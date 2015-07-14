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
import org.easybatch.core.api.event.EventManager;
import org.easybatch.core.api.event.job.JobEventListener;
import org.easybatch.core.api.event.step.*;
import org.easybatch.core.api.handler.ErrorRecordHandler;
import org.easybatch.core.api.handler.FilteredRecordHandler;
import org.easybatch.core.api.handler.IgnoredRecordHandler;
import org.easybatch.core.api.handler.RejectedRecordHandler;
import org.easybatch.core.util.Utils;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.DEFAULT_LIMIT;

/**
 * Core Easy Batch engine implementation.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
final class EngineImpl implements Engine {

    private static final Logger LOGGER = Logger.getLogger(Engine.class.getName());

    private static final String STRICT_MODE_MESSAGE = "Strict mode enabled: aborting execution";

    private String name;

    private String executionId;

    private RecordReader recordReader;

    private RecordSkipper recordSkipper;

    private FilterChain filterChain;

    private RecordMapper recordMapper;

    private ValidationPipeline validationPipeline;

    private ProcessingPipeline processingPipeline;

    private FilteredRecordHandler filteredRecordHandler;

    private IgnoredRecordHandler ignoredRecordHandler;

    private RejectedRecordHandler rejectedRecordHandler;

    private EventManager eventManager;

    private Report report;

    private boolean strictMode;

    private boolean silentMode;

    private boolean jmxEnabled;

    private long limit;

    EngineImpl(final String name,
               final RecordReader recordReader,
               final RecordSkipper recordSkipper,
               final List<RecordFilter> filters,
               final RecordMapper recordMapper,
               final List<RecordValidator> validators,
               final List<RecordProcessor> processors,
               final FilteredRecordHandler filteredRecordHandler,
               final IgnoredRecordHandler ignoredRecordHandler,
               final RejectedRecordHandler rejectedRecordHandler,
               final ErrorRecordHandler errorRecordHandler,
               final EventManager eventManager) {
        this.executionId = UUID.randomUUID().toString();
        this.name = name;
        this.limit = DEFAULT_LIMIT;
        this.recordReader = recordReader;
        this.recordSkipper = recordSkipper;
        this.recordMapper = recordMapper;
        this.filteredRecordHandler = filteredRecordHandler;
        this.ignoredRecordHandler = ignoredRecordHandler;
        this.rejectedRecordHandler = rejectedRecordHandler;
        this.report = new Report();
        this.eventManager = eventManager;
        this.filterChain = new FilterChain(filters, eventManager);
        this.validationPipeline = new ValidationPipeline(validators, eventManager);
        this.processingPipeline = new ProcessingPipeline(processors, errorRecordHandler, report, eventManager);
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

        initializeEngine();

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
                    currentRecord = readRecord();
                    if (currentRecord == null) {
                        LOGGER.log(Level.SEVERE, "The record reader returned null for next record, aborting execution");
                        reportAbortedStatus();
                        return report;
                    }
                    processedRecordsNumber++;
                    report.setCurrentRecordNumber(currentRecord.getHeader().getNumber());
                } catch (Exception e) {
                    eventManager.fireOnJobException(e);
                    eventManager.fireOnRecordReadingException(e);
                    LOGGER.log(Level.SEVERE, "An exception occurred while reading next record, aborting execution", e);
                    reportAbortedStatus();
                    return report;
                }

                /*
                 * Skip records if any
                 */
                if (recordSkipper.skipRecord(currentRecord)) {
                    report.incrementTotalSkippedRecords();
                    continue;
                }

                /*
                 * apply filter chain
                 */
                try {
                    boolean filtered = filterChain.filterRecord(currentRecord);
                    if (filtered) {
                        report.incrementTotalFilteredRecords();
                        filteredRecordHandler.handle(currentRecord);
                        continue;
                    }
                } catch (Exception e) {
                    report.incrementTotalFilteredRecords();
                    filteredRecordHandler.handle(currentRecord, e);
                    continue;
                }

                /*
                 * map record to domain object
                 */
                Object typedRecord;
                try {
                    typedRecord = mapRecord(currentRecord);
                    if (typedRecord == null) {
                        report.incrementTotalIgnoredRecord();
                        ignoredRecordHandler.handle(currentRecord);
                        continue;
                    }
                } catch (Exception e) {
                    report.incrementTotalIgnoredRecord();
                    ignoredRecordHandler.handle(currentRecord, e);
                    eventManager.fireOnJobException(e);
                    if (strictMode) {
                        reportAbortDueToStrictMode();
                        break;
                    }
                    continue;
                }

                /*
                 * apply validation pipeline
                 */
                try {
                    Set<ValidationError> validationsErrors = validateRecord(typedRecord);
                    if (!validationsErrors.isEmpty()) {
                        report.incrementTotalRejectedRecord();
                        rejectedRecordHandler.handle(currentRecord, validationsErrors);
                        if (strictMode) {
                            reportAbortDueToStrictMode();
                            break;
                        }
                        continue;
                    }
                } catch (Exception e) {
                    report.incrementTotalRejectedRecord();
                    rejectedRecordHandler.handle(currentRecord, e);
                    eventManager.fireOnJobException(e);
                    if (strictMode) {
                        reportAbortDueToStrictMode();
                        break;
                    }
                    continue;
                }

                /*
                 * apply processing pipeline
                 */
                boolean processingError = processingPipeline.process(currentRecord, typedRecord);
                if (processingError) {
                    if (strictMode) {
                        reportAbortDueToStrictMode();
                        break;
                    }
                } else {
                    report.incrementTotalSuccessRecord();
                }
            }

            tearDownEngine(processedRecordsNumber);

        } finally {
            closeRecordReader();
            eventManager.fireAfterJobEnd();
        }
        return report;

    }

    private void initializeEngine() {
        if (silentMode) {
            Utils.muteLoggers();
        }
        eventManager.fireBeforeJobStart();
        LOGGER.info("Initializing the engine");
        LOGGER.log(Level.INFO, "Engine name: {0}", getName());
        LOGGER.log(Level.INFO, "Execution id: {0}", getExecutionId());
        LOGGER.log(Level.INFO, "Strict mode: {0}", strictMode);
        LOGGER.log(Level.INFO, "Skip records: {0}", recordSkipper.getNumberOfRecordsToSkip());
        if (limit != DEFAULT_LIMIT ) {
            LOGGER.log(Level.INFO, "Records limit: {0}", limit);
        }
        report.setLimit(limit);
        report.setStartTime(System.currentTimeMillis()); //System.nanoTime() does not allow to have start time (see Javadoc)
        report.setSystemProperties(System.getProperties());
        report.setEngineName(name);
        report.setExecutionId(executionId);
    }

    private boolean initializeRecordReader() {
        try {
            openRecordReader();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception occurred while opening the record reader", e);
            eventManager.fireOnJobException(e);
            reportAbortedStatus();
            return false;
        }
        return true;
    }

    private void openRecordReader() throws RecordReaderOpeningException {
        eventManager.fireBeforeReaderOpening();
        recordReader.open();
        eventManager.fireAfterReaderOpening();
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
        LOGGER.info("The engine is running");
    }

    private void reportAbortedStatus() {
        report.setStatus(Status.ABORTED);
        report.setEndTime(System.currentTimeMillis());
    }

    private void reportAbortDueToStrictMode() {
        LOGGER.info(STRICT_MODE_MESSAGE);
        report.setStatus(Status.ABORTED);
    }

    private Record readRecord() throws RecordReadingException {
        eventManager.fireBeforeRecordReading();
        Record currentRecord = recordReader.readNextRecord();
        eventManager.fireAfterRecordReading(currentRecord);
        return currentRecord;
    }

    private Object mapRecord(Record currentRecord) throws RecordMappingException {
        Record recordToMap = eventManager.fireBeforeRecordMapping(currentRecord);
        Object typedRecord = recordMapper.mapRecord(recordToMap);
        eventManager.fireAfterRecordMapping(recordToMap, typedRecord);
        return typedRecord;
    }

    @SuppressWarnings({"unchecked"})
    private Set<ValidationError> validateRecord(Object typedRecord) {
        return validationPipeline.validateRecord(typedRecord);
    }

    private void tearDownEngine(long processedRecordsNumber) {
        report.setTotalRecords(processedRecordsNumber);
        report.setEndTime(System.currentTimeMillis());
        if (!report.getStatus().equals(Status.ABORTED)) {
            report.setStatus(Status.FINISHED);
        }

        // The batch result (if any) is held by the last processor in the pipeline (which should be of type ComputationalRecordProcessor)
        RecordProcessor lastRecordProcessor = processingPipeline.getLastProcessor();
        if (lastRecordProcessor instanceof ComputationalRecordProcessor) {
            ComputationalRecordProcessor computationalRecordProcessor = (ComputationalRecordProcessor) lastRecordProcessor;
            Object batchResult = computationalRecordProcessor.getComputationResult();
            report.setBatchResult(batchResult);
        }
    }

    private void closeRecordReader() {
        LOGGER.info("Shutting down the engine");
        eventManager.fireBeforeRecordReaderClosing();
        try {
            recordReader.close();
            eventManager.fireAfterRecordReaderClosing();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "An exception occurred while closing the record reader", e);
            eventManager.fireOnJobException(e);
        }
    }

    /*
     * Setters for engine parameters
     */

    void addRecordFilter(final RecordFilter recordFilter) {
        filterChain.addRecordFilter(recordFilter);
    }

    void setRecordReader(final RecordReader recordReader) {
        this.recordReader = recordReader;
    }

    void setRecordSkipper(RecordSkipper recordSkipper) {
        this.recordSkipper = recordSkipper;
    }

    void setRecordMapper(final RecordMapper recordMapper) {
        this.recordMapper = recordMapper;
    }

    void addRecordValidator(final RecordValidator recordValidator) {
        validationPipeline.addRecordValidator(recordValidator);
    }

    void addRecordProcessor(final RecordProcessor recordProcessor) {
        processingPipeline.addProcessor(recordProcessor);
    }

    void setFilteredRecordHandler(final FilteredRecordHandler filteredRecordHandler) {
        this.filteredRecordHandler = filteredRecordHandler;
    }

    void setIgnoredRecordHandler(final IgnoredRecordHandler ignoredRecordHandler) {
        this.ignoredRecordHandler = ignoredRecordHandler;
    }

    void setRejectedRecordHandler(final RejectedRecordHandler rejectedRecordHandler) {
        this.rejectedRecordHandler = rejectedRecordHandler;
    }

    void setErrorRecordHandler(final ErrorRecordHandler errorRecordHandler) {
        processingPipeline.setErrorRecordHandler(errorRecordHandler);
    }

    void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    void addJobEventListener(final JobEventListener jobEventListener) {
        eventManager.addJobEventListener(jobEventListener);
    }

    void addRecordReaderEventListener(final RecordReaderEventListener recordReaderEventListener) {
        eventManager.addRecordReaderEventListener(recordReaderEventListener);
    }

    void addRecordFilterEventListener(final RecordFilterEventListener recordFilterEventListener) {
        eventManager.addRecordFilterEventListener(recordFilterEventListener);
    }

    void addRecordMapperEventListener(final RecordMapperEventListener recordMapperEventListener) {
        eventManager.addRecordMapperEventListener(recordMapperEventListener);
    }

    void addRecordValidatorEventListener(final RecordValidatorEventListener recordValidatorEventListener) {
        eventManager.addRecordValidatorEventListener(recordValidatorEventListener);
    }

    void addRecordProcessorEventListener(final RecordProcessorEventListener recordProcessorEventListener) {
        eventManager.addRecordProcessorEventListener(recordProcessorEventListener);
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

    void setName(String name) {
        this.name = name;
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
