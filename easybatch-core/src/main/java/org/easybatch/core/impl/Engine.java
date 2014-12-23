/*
 * The MIT License
 *
 *   Copyright (c) 2014, Mahmoud Ben Hassine (md.benhassine@gmail.com)
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

package org.easybatch.core.impl;

import org.easybatch.core.jmx.Monitor;
import org.easybatch.core.api.*;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Core Easy Batch engine implementation.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public final class Engine implements Callable<Report> {

    private static final Logger LOGGER = Logger.getLogger(Engine.class.getName());

    public static final String STRICT_MODE_MESSAGE = "Strict mode enabled : aborting execution";

    private RecordReader recordReader;

    private RecordFilter recordFilter;

    private RecordMapper recordMapper;

    private RecordValidator recordValidator;

    private List<RecordProcessor> processingPipeline;

    private Monitor monitor;

    private Report report;

    private IgnoredRecordHandler ignoredRecordHandler;

    private RejectedRecordHandler rejectedRecordHandler;

    private ErrorRecordHandler errorRecordHandler;

    private boolean strictMode;

    Engine(final RecordReader recordReader, final RecordFilter recordFilter, final RecordMapper recordMapper,
           final RecordValidator recordValidator, final List<RecordProcessor> processingPipeline,
           final IgnoredRecordHandler ignoredRecordHandler, final RejectedRecordHandler rejectedRecordHandler, final ErrorRecordHandler errorRecordHandler) {
        this.recordReader = recordReader;
        this.recordFilter = recordFilter;
        this.recordMapper = recordMapper;
        this.recordValidator = recordValidator;
        this.processingPipeline = processingPipeline;
        this.ignoredRecordHandler = ignoredRecordHandler;
        this.rejectedRecordHandler = rejectedRecordHandler;
        this.errorRecordHandler = errorRecordHandler;
        
        report = new Report();
        monitor = new Monitor(report);
        configureJmxMBean();
    }

    @Override
    public Report call() {

        LOGGER.info("Initializing easy batch engine");
        try {
            recordReader.open();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception occurred during opening data source reader", e);
            report.setStatus(Status.ABORTED);
            return report;
        }

        String dataSourceName = recordReader.getDataSourceName();
        LOGGER.log(Level.INFO, "Data source: {0}", dataSourceName);
        report.setDataSource(dataSourceName);

        LOGGER.log(Level.INFO, "Strict mode: {0}", strictMode);
        try {

            Integer totalRecords = recordReader.getTotalRecords();
            LOGGER.log(Level.INFO, "Total records = {0}", (totalRecords == null ? "N/A" : totalRecords));
            report.setStatus(Status.RUNNING);
            LOGGER.info("easy batch engine is running...");

            report.setTotalRecords(totalRecords);
            report.setStartTime(System.currentTimeMillis()); //System.nanoTime() does not allow to have start time (see Javadoc)

            int currentRecordNumber; // the physical record number in the data source (can be different from logical record number as seen by the engine in a multi-threaded scenario)
            int processedRecordsNumber = 0;

            while (recordReader.hasNextRecord()) {

                //read next record
                Record currentRecord;
                try {
                    currentRecord = recordReader.readNextRecord();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "An exception occurred during reading next data source record", e);
                    report.setStatus(Status.ABORTED);
                    return report;
                }
                processedRecordsNumber++;
                currentRecordNumber = currentRecord.getNumber();
                report.setCurrentRecordNumber(currentRecordNumber);

                //filter record if any
                if (recordFilter.filterRecord(currentRecord)) {
                    LOGGER.log(Level.INFO, "Record #{0} [{1}] has been filtered.", new Object[]{currentRecordNumber, currentRecord});
                    report.addFilteredRecord(currentRecordNumber);
                    continue;
                }

                //map record
                Object typedRecord;
                try {
                    typedRecord = recordMapper.mapRecord(currentRecord);
                } catch (Exception e) {
                    report.addIgnoredRecord(currentRecordNumber);
                    ignoredRecordHandler.handle(currentRecord, e);
                    if (strictMode) {
                        LOGGER.info(STRICT_MODE_MESSAGE);
                        break;
                    }
                    continue;
                }

                //validate record
                try {
                    Set<ValidationError> validationsErrors = recordValidator.validateRecord(typedRecord);

                    if (!validationsErrors.isEmpty()) {
                        report.addRejectedRecord(currentRecordNumber);
                        rejectedRecordHandler.handle(currentRecord, validationsErrors);
                        continue;
                    }
                } catch(Exception e) {
                    LOGGER.log(Level.SEVERE, "An exception occurred while validating record #" + currentRecordNumber + " [" + currentRecord + "]", e);
                    report.addRejectedRecord(currentRecordNumber);
                    rejectedRecordHandler.handle(currentRecord, e);
                    if (strictMode) {
                        LOGGER.info(STRICT_MODE_MESSAGE);
                        break;
                    }
                    continue;
                }

                //execute record processing pipeline
                boolean processingError = false;
                for (RecordProcessor recordProcessor : processingPipeline) {
                    try {
                        typedRecord = recordProcessor.processRecord(typedRecord);
                    } catch (Exception e) {
                        processingError = true;
                        report.addErrorRecord(currentRecordNumber);
                        errorRecordHandler.handle(currentRecord, e);
                        break;
                    }
                }
                if (processingError) {
                    if (strictMode) {
                        LOGGER.info(STRICT_MODE_MESSAGE);
                        break;
                    }
                }
                report.addSuccessRecord(currentRecordNumber);

            }

            report.setTotalRecords(processedRecordsNumber);
            report.setEndTime(System.currentTimeMillis());
            report.setStatus(Status.FINISHED);

            // The batch result (if any) is held by the last processor in the pipeline (which should be of type ComputationalRecordProcessor)
            RecordProcessor lastRecordProcessor = processingPipeline.get(processingPipeline.size() - 1);
            if(lastRecordProcessor instanceof ComputationalRecordProcessor) {
                ComputationalRecordProcessor computationalRecordProcessor = (ComputationalRecordProcessor)lastRecordProcessor;
                Object batchResult = computationalRecordProcessor.getComputationResult();
                report.setEasyBatchResult(batchResult);
            }

        } finally {
            LOGGER.info("Shutting down easy batch engine");
            //close the record reader
            try {
                recordReader.close();
            } catch (Exception e) {
                //at this point, there is no need to log a severe message and return null as batch report
                LOGGER.log(Level.WARNING, "An exception occurred during closing data source reader", e);
            }
        }

        return report;

    }

    /*
    * Configure JMX MBean
    */
    private void configureJmxMBean() {

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name;
        try {
            name = new ObjectName("org.easybatch.core.jmx:type=EasyBatchMonitorMBean");
            if (!mbs.isRegistered(name)) {
                monitor = new Monitor(report);
                mbs.registerMBean(monitor, name);
                LOGGER.log(Level.INFO, "Easy batch JMX MBean registered successfully as: {0}", name.getCanonicalName());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to register Easy batch JMX MBean. Root exception is :" + e.getMessage(), e);
        }
    }

    void setRecordFilter(final RecordFilter recordFilter) {
        this.recordFilter = recordFilter;
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
        this.processingPipeline.add(recordProcessor);
    }

    void setIgnoredRecordHandler(final IgnoredRecordHandler ignoredRecordHandler) {
        this.ignoredRecordHandler = ignoredRecordHandler;
    }
    
    void setRejectedRecordHandler(final RejectedRecordHandler rejectedRecordHandler) {
        this.rejectedRecordHandler = rejectedRecordHandler;
    }
    
    void setErrorRecordHandler(final ErrorRecordHandler errorRecordHandler) {
        this.errorRecordHandler = errorRecordHandler;
    }
    
    void setStrictMode(final boolean strictMode) {
        this.strictMode = strictMode;
    }

}
