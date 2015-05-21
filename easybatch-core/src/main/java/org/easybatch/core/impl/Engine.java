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
import org.easybatch.core.util.Utils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Core Easy Batch engine implementation.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public final class Engine implements Callable<Report> {

    private static final Logger LOGGER = Logger.getLogger(Engine.class.getName());

    private static final String STRICT_MODE_MESSAGE = "Strict mode enabled: aborting execution";

    private RecordReader recordReader;

    private FilterChain filterChain;

    private RecordMapper recordMapper;

    private RecordValidator recordValidator;

    private ProcessingPipeline processingPipeline;

    private FilteredRecordHandler filteredRecordHandler;

    private IgnoredRecordHandler ignoredRecordHandler;

    private RejectedRecordHandler rejectedRecordHandler;

    private boolean strictMode;

    private EventManager eventManager;

    private Report report;
    
    private boolean jmxEnabled;

    Engine(final RecordReader recordReader,
           final List<RecordFilter> filters,
           final RecordMapper recordMapper,
           final RecordValidator recordValidator,
           final List<RecordProcessor> processors,
           final FilteredRecordHandler filteredRecordHandler,
           final IgnoredRecordHandler ignoredRecordHandler,
           final RejectedRecordHandler rejectedRecordHandler,
           final ErrorRecordHandler errorRecordHandler,
           final EventManager eventManager) {
        this.recordReader = recordReader;
        this.recordMapper = recordMapper;
        this.recordValidator = recordValidator;
        this.filteredRecordHandler = filteredRecordHandler;
        this.ignoredRecordHandler = ignoredRecordHandler;
        this.rejectedRecordHandler = rejectedRecordHandler;
        this.report = new Report();
        this.eventManager = eventManager;
        this.filterChain = new FilterChain(filters, eventManager);
        this.processingPipeline = new ProcessingPipeline(processors, errorRecordHandler, report, eventManager);
    }

    @Override
    public Report call() {
        eventManager.fireBeforeBatchStart();
        LOGGER.info("Initializing easy batch engine");
        try {
            openRecordReader();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception occurred during opening data source reader", e);
            eventManager.fireOnBatchException(e);
            report.setStatus(Status.ABORTED);
            report.setEndTime(System.currentTimeMillis());
            return report;
        }

        LOGGER.log(Level.INFO, "Strict mode: {0}", strictMode);

        String dataSourceName = recordReader.getDataSourceName();
        LOGGER.log(Level.INFO, "Data source: {0}", dataSourceName);
        report.setDataSource(dataSourceName);

        try {

            if(jmxEnabled) {
                LOGGER.log(Level.INFO, "Calculating the total number of records");
                Long totalRecords = recordReader.getTotalRecords();
                report.setTotalRecords(totalRecords);
                LOGGER.log(Level.INFO, "Total records = {0}", totalRecords == null ? "N/A" : totalRecords);
            }
            report.setStatus(Status.RUNNING);
            LOGGER.info("easy batch engine is running...");

            report.setStartTime(System.currentTimeMillis()); //System.nanoTime() does not allow to have start time (see Javadoc)

            long currentRecordNumber; // the physical record number in the data source (can be different from logical record number as seen by the engine in a multi-threaded scenario)
            long processedRecordsNumber = 0;

            while (recordReader.hasNextRecord()) {

                //read next record
                Record currentRecord;
                try {
                    currentRecord = readRecord();
                    if (currentRecord == null) {
                        LOGGER.log(Level.SEVERE, "The record reader returned null for next record, aborting execution.");
                        report.setStatus(Status.ABORTED);
                        report.setEndTime(System.currentTimeMillis());
                        return report;
                    }
                } catch (Exception e) {
                    eventManager.fireOnBatchException(e);
                    eventManager.fireOnRecordReadException(e);
                    LOGGER.log(Level.SEVERE, "An exception occurred during reading next data source record, aborting execution.", e);
                    report.setStatus(Status.ABORTED);
                    report.setEndTime(System.currentTimeMillis());
                    return report;
                }
                processedRecordsNumber++;
                currentRecordNumber = currentRecord.getHeader().getNumber();
                report.setCurrentRecordNumber(currentRecordNumber);

                //apply filter chain on the record
                boolean filtered = filterChain.filterRecord(currentRecord);
                if (filtered) {
                    report.addFilteredRecord(currentRecordNumber);
                    filteredRecordHandler.handle(currentRecord);
                    continue;
                }

                //map record to domain object
                Object typedRecord;
                try {
                    typedRecord = mapRecord(currentRecord);
                    if (typedRecord == null) {
                        report.addIgnoredRecord(currentRecordNumber);
                        ignoredRecordHandler.handle(currentRecord);
                        continue;
                    }
                } catch (Exception e) {
                    report.addIgnoredRecord(currentRecordNumber);
                    ignoredRecordHandler.handle(currentRecord, e);
                    eventManager.fireOnBatchException(e);
                    if (strictMode) {
                        LOGGER.info(STRICT_MODE_MESSAGE);
                        report.setStatus(Status.ABORTED);
                        break;
                    }
                    continue;
                }

                //validate record
                try {
                    Set<ValidationError> validationsErrors = validateRecord(typedRecord);

                    if (!validationsErrors.isEmpty()) {
                        report.addRejectedRecord(currentRecordNumber);
                        rejectedRecordHandler.handle(currentRecord, validationsErrors);
                        continue;
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "An exception occurred while validating record " + currentRecord, e);
                    report.addRejectedRecord(currentRecordNumber);
                    rejectedRecordHandler.handle(currentRecord, e);
                    eventManager.fireOnBatchException(e);
                    if (strictMode) {
                        LOGGER.info(STRICT_MODE_MESSAGE);
                        report.setStatus(Status.ABORTED);
                        break;
                    }
                    continue;
                }

                //execute record processing pipeline
                boolean processingError = processingPipeline.process(currentRecord, typedRecord, currentRecordNumber);
                if (processingError) {
                    if (strictMode) {
                        LOGGER.info(STRICT_MODE_MESSAGE);
                        report.setStatus(Status.ABORTED);
                        break;
                    }
                } else {
                    report.addSuccessRecord(currentRecordNumber);
                }
            }

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

        } finally {
            LOGGER.info("Shutting down easy batch engine");
            //close the record reader
            try {
                closeRecordReader();
            } catch (Exception e) {
                //at this point, there is no need to log a severe message and return null as batch report
                LOGGER.log(Level.WARNING, "An exception occurred during closing data source reader", e);
                eventManager.fireOnBatchException(e);
            }
        }
        eventManager.fireAfterBatchEnd();
        return report;

    }

    private void closeRecordReader() throws Exception {
        eventManager.fireBeforeRecordReaderClose();
        recordReader.close();
        eventManager.fireAfterRecordReaderClose();
    }

    private void openRecordReader() throws Exception {
        eventManager.fireBeforeReaderOpen();
        recordReader.open();
        eventManager.fireAfterReaderOpen();
    }

    @SuppressWarnings({"unchecked"})
    private Set<ValidationError> validateRecord(Object typedRecord) {
        eventManager.fireBeforeValidateRecord(typedRecord);
        Set<ValidationError> validationsErrors = recordValidator.validateRecord(typedRecord);
        eventManager.fireAfterValidateRecord(typedRecord, validationsErrors);
        return validationsErrors;
    }

    private Object mapRecord(Record currentRecord) throws Exception {
        Object typedRecord;
        eventManager.fireBeforeMapRecord(currentRecord);
        typedRecord = recordMapper.mapRecord(currentRecord);
        eventManager.fireAfterMapRecord(currentRecord, typedRecord);
        return typedRecord;
    }

    private Record readRecord() throws Exception {
        eventManager.fireBeforeRecordRead();
        Record currentRecord = recordReader.readNextRecord();
        eventManager.fireAfterRecordRead(currentRecord);
        return currentRecord;
    }

    /*
     * Setters for engine parameters
     */

    void addRecordFilter(final RecordFilter recordFilter) {
        this.filterChain.addRecordFilter(recordFilter);
    }

    void setRecordReader(final RecordReader recordReader) {
        this.recordReader = recordReader;
    }

    void setRecordMapper(final RecordMapper recordMapper) {
        this.recordMapper = recordMapper;
    }

    void setRecordValidator(final RecordValidator recordValidator) {
        this.recordValidator = recordValidator;
    }

    void addRecordProcessor(final RecordProcessor recordProcessor) {
        this.processingPipeline.addProcessor(recordProcessor);
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

    EventManager getEventManager() {
        return eventManager;
    }

    void setStrictMode(final boolean strictMode) {
        this.strictMode = strictMode;
    }

    void setSilentMode(boolean silentMode) {
        if (silentMode) {
            Utils.muteLoggers();
        }
    }

    void enableJMX(boolean jmx) {
        jmxEnabled = jmx;
        if (jmx) {
            Utils.registerJmxMBean(report);
        }
    }

}
