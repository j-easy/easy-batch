/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
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

package net.benas.cb4j.core.impl;

import net.benas.cb4j.core.api.*;
import net.benas.cb4j.core.config.BatchConfiguration;
import net.benas.cb4j.core.model.Record;
import net.benas.cb4j.core.util.BatchConstants;

import java.util.logging.Logger;

/**
 * Default implementation of {@link BatchEngine}.<br/>
 *
 * This class may be extended to override initializing and shutdown hooks
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class DefaultBatchEngineImpl implements BatchEngine {

    protected final Logger logger = Logger.getLogger(BatchConstants.LOGGER_CB4J);

    /*
     * CB4J services
     */
    private RecordReader recordReader;

    private RecordParser recordParser;

    protected RecordValidator recordValidator;

    protected BatchReporter batchReporter;

    protected RecordMapper recordMapper;

    protected RecordProcessor recordProcessor;

    public DefaultBatchEngineImpl(BatchConfiguration batchConfiguration) {
        this.recordReader = batchConfiguration.getRecordReader();
        this.recordParser = batchConfiguration.getRecordParser();
        this.recordValidator = batchConfiguration.getRecordValidator();
        this.recordProcessor = batchConfiguration.getRecordProcessor();
        this.batchReporter = batchConfiguration.getBatchReporter();
        this.recordMapper = batchConfiguration.getRecordMapper();
    }

    /**
     * Initialize the engine.
     * May be overridden with custom initialization code
     */
    public void init() {
        logger.info("Initializing batch");
    }

    public final void run() { //final : must not be overridden by framework users

        logger.info("CB4J engine is running...");
        final long startTime = System.currentTimeMillis();
        long currentRecordNumber = 0;
        batchReporter.setStartTime(startTime);

        while (recordReader.hasNextRecord()) {

            currentRecordNumber++;

            //parse record
            String currentRecord = recordReader.readNextRecord();
            if (!recordParser.isWellFormed(currentRecord)) {
                batchReporter.ignoreRecord(currentRecord, currentRecordNumber, recordParser.getRecordSize(currentRecord));
                continue;
            }

            //validate record
            Record currentParsedRecord = recordParser.parseRecord(currentRecord, currentRecordNumber);
            String error = recordValidator.validateRecord(currentParsedRecord);
            if (error.length() > 0) {
                batchReporter.rejectRecord(currentParsedRecord, error);
                continue;
            }

            //map record to expected type
            Object typedRecord;
            try {
                typedRecord = recordMapper.mapRecord(currentParsedRecord);
            } catch (RecordMappingException e) { //thrown by the user deliberately to reject the record
                batchReporter.rejectRecord(currentParsedRecord, e.getMessage());
                continue;
            } catch (IndexOutOfBoundsException e){ // thrown unexpectedly if trying to get field content with an invalid index in the record
                batchReporter.rejectRecord(currentParsedRecord, "Record mapping exception : " + e.getMessage());
                continue;
            }

            //process record
            recordProcessor.preProcessRecord(typedRecord);
            recordProcessor.processRecord(typedRecord);
            recordProcessor.postProcessRecord(typedRecord);
        }

        //close record reader
        recordReader.close();

        final long endTime = System.currentTimeMillis();
        batchReporter.setEndTime(endTime);
        batchReporter.setTotalInputRecords(currentRecordNumber);

    }

    /**
     * Shutdown the engine.
     * May be overridden with custom shutdown code
     */
    public void shutdown() {
        logger.info("finalizing batch");

        //generate batch report
        batchReporter.generateReport();
    }

}
