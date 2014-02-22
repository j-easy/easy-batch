/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
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

package io.github.benas.easybatch.core.impl;

import io.github.benas.easybatch.core.api.*;
import io.github.benas.easybatch.core.jmx.EasyBatchMonitor;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Core Easy Batch engine implementation.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public final class EasyBatchEngine implements Callable<EasyBatchReport> {

    private static final Logger LOGGER = Logger.getLogger(EasyBatchEngine.class.getName());

    private RecordReader recordReader;

    private RecordFilter recordFilter;

    private RecordMapper recordMapper;

    private RecordValidator recordValidator;

    private RecordProcessor recordProcessor;

    private EasyBatchMonitor easyBatchMonitor;

    private EasyBatchReport easyBatchReport;

    private boolean strictMode;

    EasyBatchEngine(final RecordReader recordReader, final RecordFilter recordFilter, final RecordMapper recordMapper,
                    final RecordValidator recordValidator, final RecordProcessor recordProcessor) {
        this.recordReader = recordReader;
        this.recordFilter = recordFilter;
        this.recordMapper = recordMapper;
        this.recordValidator = recordValidator;
        this.recordProcessor = recordProcessor;
        easyBatchReport = new EasyBatchReport();
        easyBatchMonitor = new EasyBatchMonitor(easyBatchReport);
        configureJmxMBean();
    }

    @Override
    public EasyBatchReport call() {

        LOGGER.info("Initializing easy batch engine");
        try {
            recordReader.open();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception occurred during opening data source reader", e);
            return null;
        }

        String dataSourceName = recordReader.getDataSourceName();
        LOGGER.info("Data source: " + dataSourceName);
        easyBatchReport.setDataSource(dataSourceName);

        LOGGER.info("Strict mode: " + strictMode);
        try {

            Integer totalRecords = recordReader.getTotalRecords();
            LOGGER.info("Total records = " + (totalRecords == null ? "N/A" : totalRecords));
            LOGGER.info("easy batch engine is running...");

            easyBatchReport.setTotalRecords(totalRecords);
            easyBatchReport.setStartTime(System.currentTimeMillis());

            int currentRecordNumber = 0;

            while (recordReader.hasNextRecord()) {

                long currentRecordProcessingStartTime = System.currentTimeMillis();

                //read next record
                Record currentRecord;
                try {
                    currentRecord = recordReader.readNextRecord();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "An exception occurred during reading next data source record", e);
                    return null;
                }
                currentRecordNumber = currentRecord.getNumber();
                easyBatchReport.setCurrentRecordNumber(currentRecordNumber);

                //filter record if any
                if (recordFilter.filterRecord(currentRecord)) {
                    LOGGER.log(Level.INFO, "Record #" + currentRecordNumber + " [" + currentRecord + "] has been filtered");
                    easyBatchReport.addFilteredRecord(currentRecordNumber);
                    easyBatchReport.addProcessingTime(currentRecordNumber, System.currentTimeMillis() - currentRecordProcessingStartTime);
                    continue;
                }

                //map record
                Object typedRecord;
                try {
                    typedRecord = recordMapper.mapRecord(currentRecord);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error while mapping record #" + currentRecordNumber + " [" + currentRecord + "]. Root exception:", e);
                    easyBatchReport.addIgnoredRecord(currentRecordNumber);
                    easyBatchReport.addProcessingTime(currentRecordNumber, System.currentTimeMillis() - currentRecordProcessingStartTime);
                    if (strictMode) {
                        LOGGER.info("Strict mode enabled : aborting execution");
                        break;
                    }
                    continue;
                }

                //validate record
                try {
                    Set<ValidationError> validationsErrors = recordValidator.validateRecord(typedRecord);

                    if (!validationsErrors.isEmpty()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (ValidationError validationError : validationsErrors) {
                            stringBuilder.append(validationError.getMessage()).append(" | ");
                        }
                        LOGGER.log(Level.SEVERE, "Record #" + currentRecordNumber + " [" + currentRecord + "] is not valid : " + stringBuilder.toString());
                        easyBatchReport.addRejectedRecord(currentRecordNumber);
                        easyBatchReport.addProcessingTime(currentRecordNumber, System.currentTimeMillis() - currentRecordProcessingStartTime);
                        continue;
                    }
                } catch(Exception e) {
                    LOGGER.log(Level.SEVERE, "An exception occurred while validating record #" + currentRecordNumber + " [" + currentRecord + "]", e);
                    easyBatchReport.addRejectedRecord(currentRecordNumber);
                    easyBatchReport.addProcessingTime(currentRecordNumber, System.currentTimeMillis() - currentRecordProcessingStartTime);
                    if (strictMode) {
                        LOGGER.info("Strict mode enabled : aborting execution");
                        break;
                    }
                    continue;
                }

                //process record
                try {
                    recordProcessor.processRecord(typedRecord);
                    easyBatchReport.addSuccessRecord(currentRecordNumber);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error while processing record #" + currentRecordNumber + "[" + currentRecord + "]", e);
                    easyBatchReport.addErrorRecord(currentRecordNumber);
                    if (strictMode) {
                        LOGGER.info("Strict mode enabled : aborting execution");
                        break;
                    }
                } finally {
                    //log processing time for the current record
                    easyBatchReport.addProcessingTime(currentRecordNumber, System.currentTimeMillis() - currentRecordProcessingStartTime);
                }

            }

            easyBatchReport.setTotalRecords(currentRecordNumber);
            easyBatchReport.setEndTime(System.currentTimeMillis());
            easyBatchReport.setEasyBatchResult(recordProcessor.getEasyBatchResult());

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

        return easyBatchReport;

    }

    /*
    * Configure JMX MBean
    */
    private void configureJmxMBean() {

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name;
        try {
            name = new ObjectName("io.github.benas.easybatch.core.jmx:type=EasyBatchMonitorMBean");
            if (!mbs.isRegistered(name)) {
                easyBatchMonitor = new EasyBatchMonitor(easyBatchReport);
                mbs.registerMBean(easyBatchMonitor, name);
                LOGGER.info("Easy batch JMX MBean registered successfully as: " + name.getCanonicalName());
            }
        } catch (Exception e) {
            String error = "Unable to register Easy batch JMX MBean. Root exception is :" + e.getMessage();
            LOGGER.log(Level.WARNING, error, e);
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

    void setRecordProcessor(final RecordProcessor recordProcessor) {
        this.recordProcessor = recordProcessor;
    }

    void setStrictMode(final boolean strictMode) {
        this.strictMode = strictMode;
    }

}
