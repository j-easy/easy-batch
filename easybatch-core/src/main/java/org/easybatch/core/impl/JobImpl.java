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

package org.easybatch.core.impl;

import org.easybatch.core.api.*;
import org.easybatch.core.api.listener.JobListener;
import org.easybatch.core.api.listener.PipelineListener;
import org.easybatch.core.api.listener.RecordReaderListener;
import org.easybatch.core.util.Utils;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.DEFAULT_LIMIT;
import static org.easybatch.core.util.Utils.DEFAULT_SKIP;

/**
 * Core Easy Batch job implementation.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
final class JobImpl implements Job {

    private static final Logger LOGGER = Logger.getLogger(Job.class.getName());

    private static final String STRICT_MODE_MESSAGE = "Strict mode enabled: aborting execution";

    private RecordReader recordReader;

    private Pipeline pipeline;

    private EventManager eventManager;

    private JobReport jobReport;

    JobImpl() {
        this.recordReader = new NoOpRecordReader();
        this.eventManager = new EventManager();
        this.jobReport = new JobReport();
        this.eventManager.addPipelineListener(new DefaultPipelineListener(jobReport));
        this.eventManager.addRecordReaderListener(new DefaultRecordReaderListener(jobReport));
        this.pipeline = new Pipeline(new ArrayList<RecordProcessor>(), eventManager);
    }

    @Override
    public String getName() {
        return jobReport.getParameters().getName();
    }

    @Override
    public String getExecutionId() {
        return jobReport.getParameters().getExecutionId();
    }

    @Override
    public JobReport call() {

        initializeJob();

        if (!initializeRecordReader()) {
            return jobReport;
        }

        initializeDatasource();

        setupMonitoring();

        setRunningStatus();

        try {
            long processedRecordsNumber = 0;
            while (recordReader.hasNextRecord() && processedRecordsNumber < jobReport.getParameters().getLimit()) {
                /*
                 * read next record
                 */
                Record currentRecord;
                try {
                    currentRecord = readNextRecord();
                    if (currentRecord == null) {
                        return jobReport;
                    }
                    processedRecordsNumber++;
                } catch (Exception e) {
                    eventManager.fireOnRecordReadingException(e);
                    return jobReport;
                }

                /*
                 * Skip records if any
                 */
                if (processedRecordsNumber <= jobReport.getParameters().getSkip()) {
                    jobReport.getMetrics().incrementSkippedCount();
                    continue;
                }
                
                /*
                 * Process record
                 */
                boolean processingError = pipeline.process(currentRecord);
                if (processingError && jobReport.getParameters().isStrictMode()) {
                    LOGGER.info(STRICT_MODE_MESSAGE);
                    jobReport.setStatus(JobStatus.ABORTED);
                    break;
                }
            }

            tearDownJob(processedRecordsNumber);

        } finally {
            closeRecordReader();
            eventManager.fireAfterJobEnd(jobReport);
        }
        return jobReport;

    }

    private void initializeJob() {
        if (jobReport.getParameters().isSilentMode()) {
            Utils.muteLoggers();
        }
        eventManager.fireBeforeJobStart();
        LOGGER.info("Initializing the job");
        LOGGER.log(Level.INFO, "Job name: {0}", jobReport.getParameters().getName());
        LOGGER.log(Level.INFO, "Execution id: {0}", getExecutionId());
        LOGGER.log(Level.INFO, "Strict mode: {0}", jobReport.getParameters().isStrictMode());
        if (jobReport.getParameters().getSkip() != DEFAULT_SKIP) {
            LOGGER.log(Level.INFO, "Skip records: {0}", jobReport.getParameters().getSkip());
        }
        if (jobReport.getParameters().getLimit() != DEFAULT_LIMIT ) {
            LOGGER.log(Level.INFO, "Records limit: {0}", jobReport.getParameters().getLimit());
        }
        jobReport.getMetrics().setStartTime(System.currentTimeMillis()); //System.nanoTime() does not allow to have start time (see Javadoc)
    }

    private boolean initializeRecordReader() {
        try {
            recordReader.open();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception occurred while opening the record reader", e);
            jobReport.setStatus(JobStatus.ABORTED);
            jobReport.getMetrics().setEndTime(System.currentTimeMillis());
            return false;
        }
        return true;
    }

    private void initializeDatasource() {
        String dataSourceName = recordReader.getDataSourceName();
        LOGGER.log(Level.INFO, "Data source: {0}", dataSourceName == null ? "N/A" : dataSourceName);
        jobReport.getParameters().setDataSource(dataSourceName);
    }

    private void setupMonitoring() {
        if (jobReport.getParameters().isJmxMode()) {
            LOGGER.log(Level.INFO, "Registering JMX MBean");
            Utils.registerJmxMBean(jobReport, this);
            LOGGER.log(Level.INFO, "Calculating the total number of records");
            Long totalRecords = recordReader.getTotalRecords();
            jobReport.getMetrics().setTotalCount(totalRecords);
            LOGGER.log(Level.INFO, "Total records count = {0}", totalRecords == null ? "N/A" : totalRecords);
        }
    }

    private void setRunningStatus() {
        jobReport.setStatus(JobStatus.RUNNING);
        LOGGER.info("The job is running");
    }

    private Record readNextRecord() throws RecordReadingException {
        eventManager.fireBeforeRecordReading();
        Record currentRecord = recordReader.readNextRecord();
        eventManager.fireAfterRecordReading(currentRecord);
        return currentRecord;
    }

    private void tearDownJob(long processedRecordsNumber) {
        jobReport.getMetrics().setTotalCount(processedRecordsNumber);
        jobReport.getMetrics().setEndTime(System.currentTimeMillis());
        if (!jobReport.getStatus().equals(JobStatus.ABORTED)) {
            jobReport.setStatus(JobStatus.FINISHED);
        }

        // The job result (if any) is held by the last processor in the pipeline (which should be of type ComputationalRecordProcessor)
        RecordProcessor lastRecordProcessor = pipeline.getLastProcessor();
        if (lastRecordProcessor instanceof ComputationalRecordProcessor) {
            ComputationalRecordProcessor computationalRecordProcessor = (ComputationalRecordProcessor) lastRecordProcessor;
            Object jobResult = computationalRecordProcessor.getComputationResult();
            jobReport.setJobResult(new JobResult(jobResult));
        }
    }

    private void closeRecordReader() {
        LOGGER.info("Finalizing the job");
        try {
            if (!jobReport.getParameters().isKeepAlive()) {
                recordReader.close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "An exception occurred while closing the record reader", e);
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
        return jobReport;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{name='").append(jobReport.getParameters().getName()).append('\'');
        stringBuilder.append(", executionId='").append(jobReport.getParameters().getExecutionId()).append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
