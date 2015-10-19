/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.reader.RecordReadingException;
import org.easybatch.core.record.Record;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Core Easy Batch job implementation.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
final class JobImpl implements Job {

    private static final Logger LOGGER = Logger.getLogger(Job.class.getName());

    private RecordReader recordReader;

    private Pipeline pipeline;

    private EventManager eventManager;

    private JobReport report;

    private JobParameters parameters;

    private JobMetrics metrics;

    JobImpl() {
        this.recordReader = new NoOpRecordReader();
        this.eventManager = new EventManager();
        this.report = new JobReport();
        this.parameters = report.getParameters();
        this.metrics = report.getMetrics();
        this.pipeline = new Pipeline(new ArrayList<RecordProcessor>(), eventManager);
        this.eventManager.addPipelineListener(new DefaultPipelineListener(report));
        this.eventManager.addRecordReaderListener(new DefaultRecordReaderListener(report));
        this.eventManager.addJobListener(new DefaultJobListener(report, pipeline));
        this.eventManager.addJobListener(new MonitoringSetupListener(this, report, recordReader));
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

        if (!openRecordReader()) {
            return report;
        }

        eventManager.fireBeforeJobStart(parameters);

        try {
            long processedRecordsNumber = 0;
            while (recordReader.hasNextRecord() && processedRecordsNumber < parameters.getLimit()) {

                /*
                 * Abort job if timeout is exceeded
                 */
                if (elapsedTime() >= parameters.getTimeout()) {
                    LOGGER.info("Timeout exceeded: aborting execution");
                    report.setStatus(JobStatus.ABORTED);
                    break;
                }

                /*
                 * read next record
                 */
                Record currentRecord;
                try {
                    currentRecord = readNextRecord();
                    if (currentRecord == null) {
                        return report;
                    }
                    processedRecordsNumber++;
                } catch (RecordReadingException e) {
                    eventManager.fireOnRecordReadingException(e);
                    return report;
                }

                /*
                 * Skip records if any
                 */
                if (processedRecordsNumber <= parameters.getSkip()) {
                    metrics.incrementSkippedCount();
                    continue;
                }
                
                /*
                 * Process record
                 */
                boolean processingError = pipeline.process(currentRecord);
                if (processingError && parameters.isStrictMode()) {
                    LOGGER.info("Strict mode enabled: aborting execution");
                    report.setStatus(JobStatus.ABORTED);
                    break;
                }
            }

            metrics.setTotalCount(processedRecordsNumber);

        } finally {
            closeRecordReader();
            eventManager.fireAfterJobEnd(report);
        }
        return report;

    }

    private long elapsedTime() {
        return System.currentTimeMillis() - metrics.getStartTime();
    }

    private boolean openRecordReader() {
        try {
            recordReader.open();
            String dataSourceName = recordReader.getDataSourceName();
            parameters.setDataSource(dataSourceName);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to open the record reader", e);
            report.setStatus(JobStatus.ABORTED);
            metrics.setEndTime(System.currentTimeMillis());
            return false;
        }
        return true;
    }

    private Record readNextRecord() throws RecordReadingException {
        try {
            eventManager.fireBeforeRecordReading();
            Record nextRecord = recordReader.readNextRecord();
            eventManager.fireAfterRecordReading(nextRecord);
            return nextRecord;
        } catch (Exception e) {
            throw new RecordReadingException("Unable to read next record", e);
        }
    }

    private void closeRecordReader() {
        LOGGER.info("Finalizing the job");
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

    public JobReport getJobReport() {
        return report;
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
