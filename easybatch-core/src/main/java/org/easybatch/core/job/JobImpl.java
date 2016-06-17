/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.core.job;

import org.easybatch.core.listener.JobListener;
import org.easybatch.core.listener.PipelineListener;
import org.easybatch.core.listener.RecordReaderListener;
import org.easybatch.core.processor.RecordProcessingException;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.reader.RecordReadingException;
import org.easybatch.core.record.Record;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.toSeconds;

/**
 * Core Easy Batch job implementation.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
final class JobImpl implements Job {

    private static final Logger LOGGER = Logger.getLogger(Job.class.getName());

    private RecordReader recordReader;

    private Pipeline pipeline;

    private EventManager eventManager;

    private JobReport report;

    private JobMonitor jobMonitor;

    private JobParameters parameters;

    private JobMetrics metrics;

    private boolean timedOut;

    JobImpl() {
        this.recordReader = new NoOpRecordReader();
        this.eventManager = new EventManager();
        this.report = new JobReport();
        this.parameters = report.getParameters();
        this.metrics = report.getMetrics();
        this.jobMonitor = new JobMonitor(report);
        this.pipeline = new Pipeline(new ArrayList<RecordProcessor>(), eventManager);
        this.eventManager.addPipelineListener(new DefaultPipelineListener(this));
        this.eventManager.addPipelineListener(new JobTimeoutListener(this));
        this.eventManager.addJobListener(new DefaultJobListener(this));
        this.eventManager.addJobListener(new MonitoringSetupListener(this));
    }

    @Override
    public String getName() {
        return parameters.getName();
    }

    @Override
    public String getExecutionId() {
        return parameters.getExecutionId();
    }

    @Override
    public JobReport call() {

        try {

            if (!openRecordReader()) {
                return report;
            }
            eventManager.fireBeforeJobStart(parameters);
            long recordCount = 0;
            while (recordReader.hasNextRecord() && recordCount < parameters.getLimit()) {

                //Abort job if timeout is exceeded
                if (timedOut) {
                    LOGGER.info("Timeout exceeded: aborting execution");
                    break;
                }

                //read next record
                Record currentRecord = readNextRecord();
                if (currentRecord == null) {
                    return report;
                }
                recordCount++;

                //Skip records if any
                if (shouldSkipRecord(recordCount)) {
                    metrics.incrementSkippedCount();
                    continue;
                }

                //Process record
                try {
                    pipeline.process(currentRecord);
                } catch (RecordProcessingException e) {
                    if (parameters.isStrictMode()) {
                        LOGGER.info("Strict mode enabled: aborting execution");
                        report.setStatus(JobStatus.ABORTED);
                        report.getMetrics().setLastError(e);
                        break;
                    }
                }
            }
            metrics.setTotalCount(recordCount);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "A unexpected error occurred", e);
            report.setStatus(JobStatus.FAILED);
            report.getMetrics().setLastError(e);
        } finally {
            closeRecordReader();
            eventManager.fireAfterJobEnd(report);
        }
        return report;

    }

    private Record readNextRecord() throws InterruptedException {
        Record currentRecord;
        int readAttempts = 0;
        int maxAttempts = parameters.getRetryPolicy().getMaxAttempts();
        long backOffDelay = parameters.getRetryPolicy().getBackOffDelay();
        while(readAttempts < maxAttempts) {
            try {
                readAttempts++;
                eventManager.fireBeforeRecordReading();
                currentRecord = recordReader.readNextRecord();
                eventManager.fireAfterRecordReading(currentRecord);
                return currentRecord;
            } catch (RecordReadingException e) {
                eventManager.fireOnRecordReadingException(e);
                LOGGER.log(Level.SEVERE, "Unable to read next record", e);
                report.getMetrics().setLastError(e);
                if (readAttempts >= maxAttempts) {
                    LOGGER.log(Level.WARNING, "Unable to read next record after {0} attempt(s), aborting job", maxAttempts);
                    report.setStatus(JobStatus.ABORTED);
                    report.getMetrics().setEndTime(System.currentTimeMillis());
                    return null;
                }
                Thread.sleep(backOffDelay);
                LOGGER.log(Level.INFO, "Waiting for {0}s before retrying to read next record", toSeconds(backOffDelay));
            }
        }
        return null;
    }

    private boolean shouldSkipRecord(long recordCount) {
        return recordCount <= parameters.getSkip();
    }

    private boolean openRecordReader() {
        try {
            recordReader.open();
            String dataSourceName = recordReader.getDataSourceName();
            parameters.setDataSource(dataSourceName);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to open the record reader", e);
            report.setStatus(JobStatus.FAILED);
            metrics.setEndTime(System.currentTimeMillis());
            metrics.setLastError(e);
            return false;
        }
        return true;
    }

    private void closeRecordReader() {
        LOGGER.log(Level.INFO, "Stopping job ''{0}''", parameters.getName());
        try {
            if (!parameters.isKeepAlive()) {
                recordReader.close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to close the record reader", e);
        }
    }

    /*
     * Setters for job parameters
     */

    void setRecordReader(final RecordReader recordReader) {
        this.recordReader = recordReader;
    }

    void addRecordProcessor(final RecordProcessor recordProcessor) {
        pipeline.addProcessor(recordProcessor);
    }

    void addJobListener(final JobListener jobListener) {
        eventManager.addJobListener(jobListener);
    }

    void addRecordReaderListener(final RecordReaderListener recordReaderListener) {
        eventManager.addRecordReaderListener(recordReaderListener);
    }

    void addPipelineListener(final PipelineListener pipelineListener) {
        eventManager.addPipelineListener(pipelineListener);
    }

    void setTimedOut(boolean timedOut) {
        this.timedOut = timedOut;
    }

    /*
     * Getters for job components (needed by package private artifacts)
     */

    JobReport getJobReport() {
        return report;
    }

    RecordReader getRecordReader() {
        return recordReader;
    }

    Pipeline getPipeline() {
        return pipeline;
    }

    JobMonitor getJobMonitor() {
        return jobMonitor;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{name='").append(parameters.getName()).append('\'');
        stringBuilder.append(", executionId='").append(parameters.getExecutionId()).append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
