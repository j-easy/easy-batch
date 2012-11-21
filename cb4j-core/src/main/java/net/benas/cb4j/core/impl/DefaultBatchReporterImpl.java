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

import net.benas.cb4j.core.api.BatchReporter;
import net.benas.cb4j.core.model.Record;
import net.benas.cb4j.core.util.BatchConstants;
import net.benas.cb4j.core.util.BatchStatus;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Default implementation of {@link BatchReporter}.
 *
 * Custom implementation can be registered with {@link net.benas.cb4j.core.config.BatchConfiguration}
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class DefaultBatchReporterImpl implements BatchReporter {

    /*
     * Loggers
     */
    protected final Logger logger = Logger.getLogger(BatchConstants.LOGGER_CB4J);

    protected final Logger ignoredRecordsReporter = Logger.getLogger(BatchConstants.LOGGER_CB4J_IGNORED);

    protected final Logger rejectedRecordsReporter = Logger.getLogger(BatchConstants.LOGGER_CB4J_REJECTED);

    /**
     * Total input records number.
     */
    protected long totalInputRecordsNumber;

    /**
     * Input records number.
     */
    protected long InputRecordsNumber;

    /**
     * Rejected records number.
     */
    protected long rejectedRecordsNumber;

    /**
     * Ignored records number.
     */
    protected long ignoredRecordsNumber;

    /**
     * Processed records number.
     */
    protected long processedRecordsNumber;

    /**
     * Batch execution start time.
     */
    protected long startTime;

    /**
     * Batch execution end time.
     */
    protected long endTime;

    /**
     * Batch execution status
     */
    protected BatchStatus batchStatus;

    public DefaultBatchReporterImpl() {
        rejectedRecordsNumber = 0;
        ignoredRecordsNumber = 0;
        InputRecordsNumber = 0;
        ignoredRecordsReporter.setUseParentHandlers(false);
        rejectedRecordsReporter.setUseParentHandlers(false);
    }

    /**
     * {@inheritDoc}
     */
    public void generateReport() {
        logger.info("CB4J report : ");
        logger.info("Start time = " + new Date(startTime));
        logger.info("End time = " + new Date(endTime));
        logger.info("Batch duration = " + (endTime - startTime) + "ms");
        logger.info("Total input records = " + totalInputRecordsNumber);
        logger.info("Total ignored records = " + ignoredRecordsNumber);
        logger.info("Total rejected records = " + rejectedRecordsNumber);
        logger.info("Total processed records = " + processedRecordsNumber);
    }

    /**
     * {@inheritDoc}
     */
    public void rejectRecord(final Record record, final String error) {
        rejectedRecordsNumber++;

        rejectedRecordsReporter.info("Record #" + record.getNumber() + " [" + record.getContentAsString() + "] is rejected, Error : " + error);
    }

    /**
     * {@inheritDoc}
     */
    public void ignoreRecord(final String record, final long recordNumber, final String error) {
        ignoredRecordsNumber++;

        ignoredRecordsReporter.info("Record #" + recordNumber + " [" + record + "] is ignored, Error : " + error);
    }

    /**
     * {@inheritDoc}
     */
    public void setEndTime(final long endTime) {
        this.endTime = endTime;
    }

    /**
     * {@inheritDoc}
     */
    public void setStartTime(final long startTime) {
        this.startTime = startTime;
    }

    /**
     * {@inheritDoc}
     */
    public void setTotalInputRecordsNumber(final long totalInputRecords) {
        this.totalInputRecordsNumber = totalInputRecords;
    }

    /**
     * {@inheritDoc}
     */
    public void setInputRecordsNumber(long inputRecordsNumber) {
        InputRecordsNumber = inputRecordsNumber;
    }

    /**
     * {@inheritDoc}
     */
    public void setProcessedRecordsNumber(long processedRecordsNumber) {
        this.processedRecordsNumber = processedRecordsNumber;
    }

    /**
     * {@inheritDoc}
     */
    public void setBatchStatus(BatchStatus batchStatus) {
        this.batchStatus = batchStatus;
    }

    public long getTotalInputRecordsNumber() {
        return totalInputRecordsNumber;
    }

    public long getRejectedRecordsNumber() {
        return rejectedRecordsNumber;
    }

    public long getIgnoredRecordsNumber() {
        return ignoredRecordsNumber;
    }

    public long getProcessedRecordsNumber() {
        return processedRecordsNumber;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public BatchStatus getBatchStatus() {
        return batchStatus;
    }

    public long getInputRecordsNumber() {
        return InputRecordsNumber;
    }
}
