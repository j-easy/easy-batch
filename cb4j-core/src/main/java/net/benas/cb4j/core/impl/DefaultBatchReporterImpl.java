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
     * Total input records.
     */
    protected long inputRecordsNumber;

    /**
     * Rejected records number.
     */
    protected long rejectedRecordsNumber;

    /**
     * Ignored records number.
     */
    protected long ignoredRecordsNumber;

    /**
     * Batch execution start time.
     */
    protected long startTime;

    /**
     * Batch execution end time.
     */
    protected long endTime;

    public DefaultBatchReporterImpl() {
        rejectedRecordsNumber = 0;
        ignoredRecordsNumber = 0;
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
        logger.info("Total input records = " + inputRecordsNumber);
        logger.info("Total ignored records = " + ignoredRecordsNumber);
        logger.info("Total rejected records = " + rejectedRecordsNumber);
        logger.info("Total processed records = " + (inputRecordsNumber - (rejectedRecordsNumber + ignoredRecordsNumber)));
    }

    /**
     * {@inheritDoc}
     */
    public void rejectRecord(final Record record, final String error) {
        rejectedRecordsNumber++;
        StringBuilder stringBuilder = new StringBuilder();
        rejectedRecordsReporter.info(stringBuilder.append("Record #")
                .append(record.getNumber())
                .append(" [")
                .append(record.getContentAsString())
                .append("] is rejected, Error : ")
                .append(error).toString());
    }

    /**
     * {@inheritDoc}
     */
    public void ignoreRecord(final String record, final long recordNumber, final String error) {
        ignoredRecordsNumber++;
        StringBuilder stringBuilder = new StringBuilder();
        ignoredRecordsReporter.info(stringBuilder.append("Record #")
                .append(recordNumber)
                .append(" [")
                .append(record)
                .append("] is ignored, Error : ")
                .append(error).toString());
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
    public void setTotalInputRecords(final long totalInputRecords) {
        this.inputRecordsNumber = totalInputRecords;
    }

}
