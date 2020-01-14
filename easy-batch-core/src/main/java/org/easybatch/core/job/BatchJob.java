/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package org.easybatch.core.job;

import org.easybatch.core.listener.*;
import org.easybatch.core.processor.CompositeRecordProcessor;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.writer.RecordWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.easybatch.core.job.JobStatus.*;
import static org.easybatch.core.util.Utils.formatErrorThreshold;

/**
 * Implementation of read-process-write job pattern.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
class BatchJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchJob.class);
    private static final String DEFAULT_JOB_NAME = "job";

    private String name;

    private RecordReader recordReader;
    private RecordWriter recordWriter;
    private RecordProcessor recordProcessor;
    private RecordTracker recordTracker;

    private JobListener jobListener;
    private BatchListener batchListener;
    private RecordReaderListener recordReaderListener;
    private RecordWriterListener recordWriterListener;
    private PipelineListener pipelineListener;

    private JobParameters parameters;
    private JobMetrics metrics;
    private JobReport report;
    private JobMonitor monitor;

    BatchJob(JobParameters parameters) {
        this.parameters = parameters;
        this.name = DEFAULT_JOB_NAME;
        metrics = new JobMetrics();
        report = new JobReport();
        report.setParameters(parameters);
        report.setMetrics(metrics);
        report.setJobName(name);
        report.setSystemProperties(System.getProperties());
        monitor = new JobMonitor(report);
        recordReader = new NoOpRecordReader();
        recordProcessor = new CompositeRecordProcessor();
        recordWriter = new NoOpRecordWriter();
        recordReaderListener = new CompositeRecordReaderListener();
        pipelineListener = new CompositePipelineListener();
        recordWriterListener = new CompositeRecordWriterListener();
        batchListener = new CompositeBatchListener();
        jobListener = new CompositeJobListener();
        recordTracker = new RecordTracker();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JobReport call() {
        start();
        try {
            openReader();
            openWriter();
            setStatus(STARTED);
            while (moreRecords() && !isInterrupted()) {
                Batch batch = readAndProcessBatch();
                writeBatch(batch);
            }
            setStatus(STOPPING);
        } catch (Exception exception) {
            fail(exception);
            return report;
        } finally {
            closeReader();
            closeWriter();
        }
        teardown();
        return report;
    }

    /*
     * private methods
     */

    private void start() {
        setStatus(STARTING);
        jobListener.beforeJobStart(parameters);
        recordTracker = new RecordTracker();
        metrics.setStartTime(System.currentTimeMillis());
        LOGGER.info("Batch size: {}", parameters.getBatchSize());
        LOGGER.info("Error threshold: {}", formatErrorThreshold(parameters.getErrorThreshold()));
        LOGGER.info("Jmx monitoring: {}", parameters.isJmxMonitoring());
        registerJobMonitor();
    }

    private void registerJobMonitor() {
        if (parameters.isJmxMonitoring()) {
            monitor.registerJmxMBeanFor(this);
        }
    }

    private void openReader() throws RecordReaderOpeningException {
        try {
            LOGGER.debug("Opening record reader");
            recordReader.open();
        } catch (Exception e) {
            throw new RecordReaderOpeningException("Unable to open record reader", e);
        }
    }

    private void openWriter() throws RecordWriterOpeningException {
        try {
            LOGGER.debug("Opening record writer");
            recordWriter.open();
        } catch (Exception e) {
            throw new RecordWriterOpeningException("Unable to open record writer", e);
        }
    }

    private void setStatus(JobStatus status) {
        if(isInterrupted()) {
            LOGGER.info("Job '{}' has been interrupted, aborting execution.", name);
        }
        LOGGER.info("Job '{}' {}", name, status.name().toLowerCase());
        report.setStatus(status);
    }

    private boolean moreRecords() {
        return recordTracker.moreRecords();
    }

    private Batch readAndProcessBatch() throws RecordReadingException, ErrorThresholdExceededException {
        Batch batch = new Batch();
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
        return batch;
    }

    private Record readRecord() throws RecordReadingException {
        Record record;
        try {
            LOGGER.debug("Reading next record");
            recordReaderListener.beforeRecordReading();
            record = recordReader.readRecord();
            recordReaderListener.afterRecordReading(record);
            return record;
        } catch (Exception e) {
            recordReaderListener.onRecordReadingException(e);
            throw new RecordReadingException("Unable to read next record", e);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void processRecord(Record record, Batch batch) throws ErrorThresholdExceededException {
        Record processedRecord = null;
        try {
            LOGGER.debug("Processing {}", record);
            notifyJobUpdate();
            Record preProcessedRecord = pipelineListener.beforeRecordProcessing(record);
            if (preProcessedRecord == null) {
                LOGGER.debug("{} has been filtered", record);
                metrics.incrementFilterCount();
            } else {
                processedRecord = recordProcessor.processRecord(preProcessedRecord);
                if (processedRecord == null) {
                    LOGGER.debug("{} has been filtered", record);
                    metrics.incrementFilterCount();
                } else {
                    batch.addRecord(processedRecord);
                }
            }
            pipelineListener.afterRecordProcessing(record, processedRecord);
        } catch (Exception e) {
            LOGGER.error("Unable to process {}", record, e);
            pipelineListener.onRecordProcessingException(record, e);
            metrics.incrementErrorCount();
            report.setLastError(e);
            if (metrics.getErrorCount() > parameters.getErrorThreshold()) {
                throw new ErrorThresholdExceededException("Error threshold exceeded. Aborting execution", e);
            }
        }
    }

    private void writeBatch(Batch batch) throws BatchWritingException {
        LOGGER.debug("Writing {}", batch);
        try {
            if (!batch.isEmpty()) {
                recordWriterListener.beforeRecordWriting(batch);
                recordWriter.writeRecords(batch);
                recordWriterListener.afterRecordWriting(batch);
                batchListener.afterBatchWriting(batch);
                metrics.incrementWriteCount(batch.size());
            }
        } catch (Exception e) {
            recordWriterListener.onRecordWritingException(batch, e);
            batchListener.onBatchWritingException(batch, e);
            throw new BatchWritingException("Unable to write records", e);
        }
    }

    private boolean isInterrupted() {
        return Thread.currentThread().isInterrupted();
    }

    private void teardown() {
        JobStatus jobStatus = isInterrupted() ? ABORTED : COMPLETED;
        teardown(jobStatus);
    }

    private void teardown(JobStatus status) {
        report.setStatus(status);
        metrics.setEndTime(System.currentTimeMillis());
        LOGGER.info( "Job '{}' finished with status: {}", new Object[]{name, report.getStatus()});
        notifyJobUpdate();
        jobListener.afterJobEnd(report);
    }

    private void fail(Exception exception) {
        String reason = exception.getMessage();
        Throwable error = exception.getCause();
        LOGGER.error(reason, error);
        report.setLastError(error);
        teardown(FAILED);
    }

    private void closeReader() {
        try {
            LOGGER.debug("Closing record reader");
            recordReader.close();
        } catch (Exception e) {
            LOGGER.warn("Unable to close record reader", e);
            report.setLastError(e);
        }
    }

    private void closeWriter() {
        try {
            LOGGER.debug("Closing record writer");
            recordWriter.close();
        } catch (Exception e) {
            LOGGER.warn("Unable to close record writer", e);
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
