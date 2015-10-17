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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.*;

/**
 * Core Easy Batch job implementation.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
final class JobImpl implements Job {

    private static final Logger LOGGER = Logger.getLogger(Job.class.getName());

    private static final String STRICT_MODE_MESSAGE = "Strict mode enabled: aborting execution";

    private String name;

    private String executionId;

    private RecordReader recordReader;

    private Pipeline pipeline;

    private EventManager eventManager;

    private Report report;

    private boolean strictMode;

    private boolean silentMode;

    private boolean jmxEnabled;

    private boolean keepAlive;

    private long limit;
    
    private long skip;

    JobImpl() {
        this.executionId = UUID.randomUUID().toString();
        this.name = DEFAULT_JOB_NAME;
        this.limit = DEFAULT_LIMIT;
        this.skip = DEFAULT_SKIP;
        this.recordReader = new NoOpRecordReader();
        this.report = new Report();
        this.eventManager = new EventManager();
        this.eventManager.addPipelineListener(new DefaultPipelineListener(report));
        this.eventManager.addRecordReaderListener(new DefaultRecordReaderListener(report));
        this.pipeline = new Pipeline(new ArrayList<RecordProcessor>(), eventManager);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExecutionId() {
        return executionId;
    }

    @Override
    public Report call() {

        initializeJob();

        if (!initializeRecordReader()) {
            return report;
        }

        initializeDatasource();

        setupMonitoring();

        setRunningStatus();

        try {
            long processedRecordsNumber = 0;
            while (recordReader.hasNextRecord() && processedRecordsNumber < limit) {
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
                    report.setCurrentRecordNumber(currentRecord.getHeader().getNumber());
                } catch (Exception e) {
                    eventManager.fireOnRecordReadingException(e);
                    return report;
                }

                /*
                 * Skip records if any
                 */
                if (processedRecordsNumber <= skip) {
                    report.incrementTotalSkippedRecords();
                    continue;
                }
                
                /*
                 * Process record
                 */
                boolean processingError = pipeline.process(currentRecord);
                if (processingError && strictMode) {
                    LOGGER.info(STRICT_MODE_MESSAGE);
                    report.setStatus(Status.ABORTED);
                    break;
                }
            }

            tearDownJob(processedRecordsNumber);

        } finally {
            closeRecordReader();
            eventManager.fireAfterJobEnd(report);
        }
        return report;

    }

    private void initializeJob() {
        if (silentMode) {
            Utils.muteLoggers();
        }
        eventManager.fireBeforeJobStart();
        LOGGER.info("Initializing the job");
        LOGGER.log(Level.INFO, "Job name: {0}", getName());
        LOGGER.log(Level.INFO, "Execution id: {0}", getExecutionId());
        LOGGER.log(Level.INFO, "Strict mode: {0}", strictMode);
        if (skip != DEFAULT_SKIP) {
            LOGGER.log(Level.INFO, "Skip records: {0}", skip);
        }
        if (limit != DEFAULT_LIMIT ) {
            LOGGER.log(Level.INFO, "Records limit: {0}", limit);
        }
        report.setLimit(limit);
        report.setStartTime(System.currentTimeMillis()); //System.nanoTime() does not allow to have start time (see Javadoc)
        report.setSystemProperties(System.getProperties());
        report.setName(name);
        report.setExecutionId(executionId);
    }

    private boolean initializeRecordReader() {
        try {
            recordReader.open();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception occurred while opening the record reader", e);
            report.setStatus(Status.ABORTED);
            report.setEndTime(System.currentTimeMillis());
            return false;
        }
        return true;
    }

    private void initializeDatasource() {
        String dataSourceName = recordReader.getDataSourceName();
        LOGGER.log(Level.INFO, "Data source: {0}", dataSourceName == null ? "N/A" : dataSourceName);
        report.setDataSource(dataSourceName);
    }

    private void setupMonitoring() {
        if (jmxEnabled) {
            LOGGER.log(Level.INFO, "Registering JMX MBean");
            Utils.registerJmxMBean(report, this);
            LOGGER.log(Level.INFO, "Calculating the total number of records");
            Long totalRecords = recordReader.getTotalRecords();
            report.setTotalRecords(totalRecords);
            LOGGER.log(Level.INFO, "Total records = {0}", totalRecords == null ? "N/A" : totalRecords);
        }
    }

    private void setRunningStatus() {
        report.setStatus(Status.RUNNING);
        LOGGER.info("The job is running");
    }

    private Record readNextRecord() throws RecordReadingException {
        eventManager.fireBeforeRecordReading();
        Record currentRecord = recordReader.readNextRecord();
        eventManager.fireAfterRecordReading(currentRecord);
        return currentRecord;
    }

    private void tearDownJob(long processedRecordsNumber) {
        report.setTotalRecords(processedRecordsNumber);
        report.setEndTime(System.currentTimeMillis());
        if (!report.getStatus().equals(Status.ABORTED)) {
            report.setStatus(Status.FINISHED);
        }

        // The job result (if any) is held by the last processor in the pipeline (which should be of type ComputationalRecordProcessor)
        RecordProcessor lastRecordProcessor = pipeline.getLastProcessor();
        if (lastRecordProcessor instanceof ComputationalRecordProcessor) {
            ComputationalRecordProcessor computationalRecordProcessor = (ComputationalRecordProcessor) lastRecordProcessor;
            Object jobResult = computationalRecordProcessor.getComputationResult();
            report.setJobResult(jobResult);
        }
    }

    private void closeRecordReader() {
        LOGGER.info("Shutting down the job");
        try {
            if (!keepAlive) {
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

    void setStrictMode(final boolean strictMode) {
        this.strictMode = strictMode;
    }

    void setSilentMode(boolean silentMode) {
        this.silentMode = silentMode;
    }

    void setLimit(final long limit) {
        this.limit = limit;
    }

    void setSkip(long skip) {
        this.skip = skip;
    }

    void setName(String name) {
        this.name = name;
    }

    void setKeepAlive(final boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    void enableJMX(boolean jmx) {
        jmxEnabled = jmx;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{name='").append(name).append('\'');
        stringBuilder.append(", executionId='").append(executionId).append('\'');
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
