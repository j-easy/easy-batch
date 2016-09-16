package org.easybatch.core.job;

import org.easybatch.core.listener.*;
import org.easybatch.core.processor.CompositeRecordProcessor;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Record;
import org.easybatch.core.writer.RecordWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Implementation of read-process-write job pattern.
 *
 * <strong>Each instance of this class corresponds to a job run (ie execution). Running the same instance twice will throw an {@code IllegalStateException}.</strong>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BatchJob implements Job {

    private static final Logger LOGGER = Logger.getLogger(BatchJob.class.getName());

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
    private boolean executed;

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
        return parameters.getName();
    }

    @Override
    public String getExecutionId() {
        return parameters.getExecutionId();
    }

    public BatchJob(JobParameters parameters) {
        this.parameters = parameters;
        metrics = new JobMetrics();
        report = new JobReport();
        report.setParameters(parameters);
        report.setMetrics(metrics);
        monitor = new JobMonitor(report);
        setRecordReader(new NoOpRecordReader());
        setRecordReaderListener(new NoOpRecordReaderListener());
        setRecordProcessor(new CompositeRecordProcessor());
        setPipelineListener(new NoOpPipelineListener());
        setRecordWriter(new NoOpRecordWriter());
        setRecordWriterListener(new NoOpRecordWriterListener());
        setBatchListener(new NoOpBatchListener());
        setJobListener(new NoOpJobListener());
    }

    @Override
    public JobReport call() {
        if (executed) {
            throw new IllegalStateException("Job already executed with execution id = " + parameters.getExecutionId());
        }

        report.setStatus(JobStatus.STARTING);
        metrics.setStartTime(System.currentTimeMillis());
        LOGGER.log(Level.INFO, "Starting job ''{0}''", parameters.getName());
        LOGGER.log(Level.INFO, "Execution id: {0}", parameters.getExecutionId());
        LOGGER.log(Level.INFO, "Batch size: {0}", parameters.getBatchSize());
        LOGGER.log(Level.INFO, "Error threshold: {0}", parameters.getErrorThreshold());
        LOGGER.log(Level.INFO, "Jmx mode: {0}", parameters.isJmxMode());

        jobListener.beforeJobStart(parameters);
        if (parameters.isJmxMode()) {
            monitor.registerJmxMBeanFor(this);
        }

        /*
         * Open reader
         */
        try {
            recordReader.open();
        } catch (Exception e) {
            executed = true;
            report.setStatus(JobStatus.FAILED);
            metrics.setEndTime(System.currentTimeMillis());
            report.setLastError(e);
            return report;
        }

        /*
         * Open writer
         */
        try {
            recordWriter.open();
        } catch (Exception e) {
            executed = true;
            report.setStatus(JobStatus.FAILED);
            metrics.setEndTime(System.currentTimeMillis());
            report.setLastError(e);
            return report;
        }

        LOGGER.log(Level.INFO, "Job ''{0}'' started", parameters.getName());
        report.setStatus(JobStatus.STARTED);
        boolean moreRecords = true;

        while (moreRecords) {

            /*
             * Process batch
             */
            batchListener.beforeBatch();

            List<Record> batch = new ArrayList<>();
            for (int i = 0; i < parameters.getBatchSize(); i++) {

                /*
                 * Read record
                 */
                Record record;
                try {
                    recordReaderListener.beforeRecordReading();
                    record = recordReader.readRecord();
                    recordReaderListener.afterRecordReading(record);
                    if (record == null) {
                        moreRecords = false;
                        break;
                    } else {
                        metrics.incrementReadCount();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    recordReaderListener.onRecordReadingException(e);
                    executed = true;
                    report.setStatus(JobStatus.FAILED);
                    metrics.setEndTime(System.currentTimeMillis());
                    report.setLastError(e);
                    return report;
                }

                /*
                 * Process record
                 */
                Record processedRecord;
                try {
                    pipelineListener.beforeRecordProcessing(record);
                    if (parameters.isJmxMode()) {
                        monitor.notifyJobReportUpdate();
                    }
                    processedRecord = recordProcessor.processRecord(record);
                    pipelineListener.afterRecordProcessing(record, processedRecord);
                    if (processedRecord == null) {
                        LOGGER.log(Level.INFO, "{0} has been filtered", record);
                        metrics.incrementFilteredCount();
                    } else {
                        batch.add(processedRecord);
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Unable to process " + record, e);
                    pipelineListener.onRecordProcessingException(record, e);
                    metrics.incrementErrorCount();
                    report.setLastError(e);
                    if (metrics.getErrorCount() > parameters.getErrorThreshold()) {
                        LOGGER.log(Level.SEVERE, "Error threshold exceeded. Aborting execution");
                        executed = true;
                        report.setStatus(JobStatus.FAILED);
                        metrics.setEndTime(System.currentTimeMillis());
                        return report;
                    }
                }

            }

            /*
             * Write records
             */
            try {
                if (!batch.isEmpty()) {
                    recordWriterListener.beforeRecordWriting(batch);
                    recordWriter.writeRecords(batch);
                    recordWriterListener.afterRecordWriting(batch);
                    metrics.incrementWriteCount(batch.size());
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Unable to write records", e);
                recordWriterListener.onRecordWritingException(batch, e);
                executed = true;
                report.setStatus(JobStatus.FAILED);
                metrics.setEndTime(System.currentTimeMillis());
                report.setLastError(e);
                return report;
            }

            batchListener.afterBatch(batch);
            /*
             * End process batch
             */
        }

        report.setStatus(JobStatus.STOPPING);
        try {
            recordReader.close();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to close record reader", e);
            report.setLastError(e);
        }
        try {
            recordWriter.close();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to close record writer", e);
            report.setLastError(e);
        }

        report.setStatus(JobStatus.COMPLETED);
        metrics.setEndTime(System.currentTimeMillis());
        executed = true;
        LOGGER.log(Level.INFO, "Job ''{0}'' finished with exit status: {1}", new Object[]{parameters.getName(), report.getStatus()});
        if (parameters.isJmxMode()) {
            monitor.notifyJobReportUpdate();
        }
        jobListener.afterJobEnd(report);
        return report;
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

    private void setRecordProcessor(RecordProcessor recordProcessor) {
        this.recordProcessor = recordProcessor;
    }

    public void addRecordProcessor(RecordProcessor recordProcessor) {
        ((CompositeRecordProcessor)this.recordProcessor).addRecordProcessor(recordProcessor);
    }

    public void setBatchListener(BatchListener batchListener) {
        this.batchListener = batchListener;
    }

    public void setJobListener(JobListener jobListener) {
        this.jobListener = jobListener;
    }

    public void setRecordReaderListener(RecordReaderListener recordReaderListener) {
        this.recordReaderListener = recordReaderListener;
    }

    public void setRecordWriterListener(RecordWriterListener recordWriterListener) {
        this.recordWriterListener = recordWriterListener;
    }

    public void setPipelineListener(PipelineListener pipelineListener) {
        this.pipelineListener = pipelineListener;
    }
}
