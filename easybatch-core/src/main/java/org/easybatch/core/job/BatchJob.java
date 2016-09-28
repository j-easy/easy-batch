package org.easybatch.core.job;

import org.easybatch.core.listener.*;
import org.easybatch.core.processor.CompositeRecordProcessor;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.writer.RecordWriter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.easybatch.core.job.JobStatus.*;
import static org.easybatch.core.util.Utils.formatErrorThreshold;

/**
 * Implementation of read-process-write job pattern.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class BatchJob implements Job {

    private static final Logger LOGGER = Logger.getLogger(BatchJob.class.getName());
    private static final String DEFAULT_JOB_NAME = "job";

    private String name;

    private RecordReader recordReader;
    private RecordWriter recordWriter;
    private RecordProcessor recordProcessor;

    private JobListener jobListener;
    private BatchListener batchListener;
    private RecordReaderListener recordReaderListener;
    private RecordWriterListener recordWriterListener;
    private PipelineListener pipelineListener;

    private JobParameters parameters;
    private JobMetrics metrics;
    private JobReport report;
    private JobMonitor monitor;

    static {
        try {
            if (System.getProperty("java.util.logging.config.file") == null &&
                    System.getProperty("java.util.logging.config.class") == null) {
                LogManager.getLogManager().readConfiguration(BatchJob.class.getResourceAsStream("/logging.properties"));
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unable to load logging configuration file", e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    BatchJob(JobParameters parameters) {
        this.parameters = parameters;
        this.name = DEFAULT_JOB_NAME;
        metrics = new JobMetrics();
        report = new JobReport();
        report.setParameters(parameters);
        report.setMetrics(metrics);
        report.setJobName(name);
        monitor = new JobMonitor(report);
        recordReader = new NoOpRecordReader();
        recordProcessor = new CompositeRecordProcessor();
        recordWriter = new NoOpRecordWriter();
        recordReaderListener = new CompositeRecordReaderListener();
        pipelineListener = new CompositePipelineListener();
        recordWriterListener = new CompositeRecordWriterListener();
        batchListener = new CompositeBatchListener();
        jobListener = new CompositeJobListener();
    }

    @Override
    public JobReport call() {
        start();
        Batch batch = new Batch();
        try {
            openReader();
            openWriter();
            setStatus(STARTED);
            RecordTracker recordTracker = new RecordTracker();
            while (recordTracker.moreRecords()) {
                batch = new Batch();
                processBatch(batch, recordTracker);
                writeBatch(batch);
            }
            setStatus(STOPPING);
        } catch (Exception exception) {
            fail(exception);
            handleSpecificException(exception, batch);
            return report;
        } finally {
            closeReader();
            closeWriter();
        }
        complete();
        return report;
    }

    /*
     * private methods
     */

    private void start() {
        setStatus(STARTING);
        jobListener.beforeJobStart(parameters);
        metrics.setStartTime(System.currentTimeMillis());
        LOGGER.log(Level.INFO, "Batch size: {0}", parameters.getBatchSize());
        LOGGER.log(Level.INFO, "Error threshold: {0}", formatErrorThreshold(parameters.getErrorThreshold()));
        LOGGER.log(Level.INFO, "Jmx monitoring: {0}", parameters.isJmxMonitoring());
        registerJobMonitor();
    }

    private void registerJobMonitor() {
        if (parameters.isJmxMonitoring()) {
            monitor.registerJmxMBeanFor(this);
        }
    }

    private void openReader() throws RecordReaderOpeningException {
        try {
            recordReader.open();
        } catch (Exception e) {
            throw new RecordReaderOpeningException("Unable to open record reader", e);
        }
    }

    private void openWriter() throws RecordWriterOpeningException {
        try {
            recordWriter.open();
        } catch (Exception e) {
            throw new RecordWriterOpeningException("Unable to open record writer", e);
        }
    }

    private void setStatus(JobStatus status) {
        LOGGER.log(Level.INFO, "Job ''{0}'' " + status.name().toLowerCase(), name);
        report.setStatus(status);
    }

    private void processBatch(Batch batch, RecordTracker recordTracker) throws RecordReadingException, ErrorThresholdExceededException {
        batchListener.beforeBatchReading();
        for (int i = 0; i < parameters.getBatchSize(); i++) {
            Record record = readRecord();
            if (record == null) {
                recordTracker.noMoreRecords();
                break;
            } else {
                metrics.incrementReadCount();
            }
            processRecord(record, batch);
        }
        batchListener.afterBatchProcessing(batch);
    }

    private Record readRecord() throws RecordReadingException {
        Record record;
        try {
            recordReaderListener.beforeRecordReading();
            record = recordReader.readRecord();
            recordReaderListener.afterRecordReading(record);
            return record;
        } catch (Exception e) {
            throw new RecordReadingException("Unable to read next record", e);
        }
    }

    private void processRecord(Record record, Batch batch) throws ErrorThresholdExceededException {
        Record processedRecord;
        try {
            notifyJobUpdate();
            pipelineListener.beforeRecordProcessing(record);
            processedRecord = recordProcessor.processRecord(record);
            pipelineListener.afterRecordProcessing(record, processedRecord);
            if (processedRecord == null) {
                LOGGER.log(Level.INFO, "{0} has been filtered", record);
                metrics.incrementFilteredCount();
            } else {
                batch.addRecord(processedRecord);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to process " + record, e);
            pipelineListener.onRecordProcessingException(record, e);
            metrics.incrementErrorCount();
            report.setLastError(e);
            if (metrics.getErrorCount() > parameters.getErrorThreshold()) {
                throw new ErrorThresholdExceededException("Error threshold exceeded. Aborting execution", e);
            }
        }
    }

    private void writeBatch(Batch batch) throws BatchWritingException {
        try {
            if (!batch.isEmpty()) {
                recordWriterListener.beforeRecordWriting(batch);
                recordWriter.writeRecords(batch);
                recordWriterListener.afterRecordWriting(batch);
                batchListener.afterBatchWriting(batch);
                metrics.incrementWriteCount(batch.size());
            }
        } catch (Exception e) {
            throw new BatchWritingException("Unable to write records", e);
        }
    }

    private void handleSpecificException(Exception exception, Batch batch) {
        if (exception instanceof BatchWritingException) {
            recordWriterListener.onRecordWritingException(batch, exception.getCause());
            batchListener.onBatchWritingException(batch, exception.getCause());
        }
        if (exception instanceof RecordReadingException) {
            recordReaderListener.onRecordReadingException(exception.getCause());
        }
    }

    private void teardown(JobStatus status) {
        report.setStatus(status);
        metrics.setEndTime(System.currentTimeMillis());
        LOGGER.log(Level.INFO, "Job ''{0}'' finished with status: {1}", new Object[]{name, report.getStatus()});
        notifyJobUpdate();
        jobListener.afterJobEnd(report);
    }

    private void complete() {
        teardown(COMPLETED);
    }

    private void fail(Exception exception) {
        String reason = exception.getMessage();
        Throwable error = exception.getCause();
        LOGGER.log(Level.SEVERE, reason, error);
        report.setLastError(error);
        teardown(FAILED);
    }

    private void closeReader() {
        try {
            recordReader.close();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to close record reader", e);
            report.setLastError(e);
        }
    }

    private void closeWriter() {
        try {
            recordWriter.close();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to close record writer", e);
            report.setLastError(e);
        }
    }

    private void notifyJobUpdate() {
        if (parameters.isJmxMonitoring()) {
            monitor.notifyJobReportUpdate();
        }
    }

    /*
     * Setters for job components
     */

    public void setRecordReader(RecordReader recordReader) {
        this.recordReader = recordReader;
    }

    public void setRecordWriter(RecordWriter recordWriter) {
        this.recordWriter = recordWriter;
    }

    public void addRecordProcessor(RecordProcessor recordProcessor) {
        ((CompositeRecordProcessor) this.recordProcessor).addRecordProcessor(recordProcessor);
    }

    public void addBatchListener(BatchListener batchListener) {
        ((CompositeBatchListener) this.batchListener).addBatchListener(batchListener);
    }

    public void addJobListener(JobListener jobListener) {
        ((CompositeJobListener) this.jobListener).addJobListener(jobListener);
    }

    public void addRecordReaderListener(RecordReaderListener recordReaderListener) {
        ((CompositeRecordReaderListener) this.recordReaderListener).addRecordReaderListener(recordReaderListener);
    }

    public void addRecordWriterListener(RecordWriterListener recordWriterListener) {
        ((CompositeRecordWriterListener) this.recordWriterListener).addRecordWriterListener(recordWriterListener);
    }

    public void addPipelineListener(PipelineListener pipelineListener) {
        ((CompositePipelineListener) this.pipelineListener).addPipelineListener(pipelineListener);
    }

    public void setName(String name) {
        this.name = name;
        this.report.setJobName(name);
    }
}
