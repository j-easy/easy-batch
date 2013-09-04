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

import net.benas.cb4j.core.api.BatchReport;
import net.benas.cb4j.core.api.BatchReporter;
import net.benas.cb4j.core.api.BatchResultHolder;
import net.benas.cb4j.core.model.Record;
import net.benas.cb4j.core.util.BatchConstants;
import net.benas.cb4j.core.util.BatchStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
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

    protected final Logger errorRecordsReporter = Logger.getLogger(BatchConstants.LOGGER_CB4J_ERRORS);

    /**
     * Batch report instance.
     */
    protected BatchReport batchReport;

    /**
     * Batch execution status.
     */
    protected BatchStatus batchStatus;

    public DefaultBatchReporterImpl() {
        ignoredRecordsReporter.setUseParentHandlers(false);
        rejectedRecordsReporter.setUseParentHandlers(false);
        errorRecordsReporter.setUseParentHandlers(false);
    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        batchReport = new BatchReport();
        batchReport.setTotalInputRecordsNumber(0);
        batchReport.setIgnoredRecordsNumber(0);
        batchReport.setRejectedRecordsNumber(0);
        batchReport.setProcessedRecordsNumber(0);
        batchReport.setErrorRecordsNumber(0);
    }

    /**
     * {@inheritDoc}
     */
    public void generateReport() {
        logger.info("CB4J report : ");
        long startTime = batchReport.getStartTime();
        long endTime = batchReport.getEndTime();
        logger.info("Start time = " + new Date(startTime));
        logger.info("End time = " + new Date(endTime));
        logger.info("Batch duration = " + (endTime - startTime) + "ms");
        logger.info("Total input records = " + batchReport.getTotalInputRecordsNumber());
        logger.info("Total ignored records = " + batchReport.getIgnoredRecordsNumber());
        logger.info("Total rejected records = " + batchReport.getRejectedRecordsNumber());
        logger.info("Total records processed successfully = " + Math.max(0, batchReport.getProcessedRecordsNumber() - batchReport.getErrorRecordsNumber() - batchReport.getIgnoredRecordsNumber() - batchReport.getRejectedRecordsNumber() ));
        logger.info("Total records processed with error = " + batchReport.getErrorRecordsNumber());
    }

    /**
     * {@inheritDoc}
     */
    public void reportRejectedRecord(final Record record, final String error) {
        batchReport.setRejectedRecordsNumber(batchReport.getRejectedRecordsNumber() + 1);
        rejectedRecordsReporter.info(formatError(record, error, null, "is rejected, Error : "));
    }

    /**
     * {@inheritDoc}
     */
    public void reportRejectedRecord(final Record record, final String error, final Throwable throwable) {
        batchReport.setRejectedRecordsNumber(batchReport.getRejectedRecordsNumber() + 1);
        rejectedRecordsReporter.info(formatError(record, error, throwable, "is rejected, Error : "));
    }

    /**
     * {@inheritDoc}
     */
    public void reportIgnoredRecord(final String record, final long recordNumber, final String error) {
        batchReport.setIgnoredRecordsNumber(batchReport.getIgnoredRecordsNumber() + 1);
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
    public void reportErrorRecord(final Record record, final String error, final Throwable throwable) {
        batchReport.setErrorRecordsNumber(batchReport.getErrorRecordsNumber() + 1);
        errorRecordsReporter.info(formatError(record, error, throwable, "processed with error, "));
    }

    /**
     * {@inheritDoc}
     */
    public void reportErrorRecord(final Record record, final String error) {
        batchReport.setErrorRecordsNumber(batchReport.getErrorRecordsNumber() + 1);
        errorRecordsReporter.info(formatError(record, error, null, "processed with error, "));
    }

    /**
     * {@inheritDoc}
     */
    public void setEndTime(final long endTime) {
        batchReport.setEndTime(endTime);
    }

    /**
     * {@inheritDoc}
     */
    public void setStartTime(final long startTime) {
        batchReport.setStartTime(startTime);
    }

    /**
     * {@inheritDoc}
     */
    public void setTotalInputRecordsNumber(final long totalInputRecords) {
        batchReport.setTotalInputRecordsNumber(totalInputRecords);
    }

    /**
     * {@inheritDoc}
     */
    public void setInputRecordsNumber(long inputRecordsNumber) {
        batchReport.setInputRecordsNumber(inputRecordsNumber);
    }

    /**
     * {@inheritDoc}
     */
    public void setProcessedRecordsNumber(long processedRecordsNumber) {
        batchReport.setProcessedRecordsNumber(processedRecordsNumber);
    }

    /**
     * {@inheritDoc}
     */
    public void setBatchStatus(BatchStatus batchStatus) {
        this.batchStatus = batchStatus;
    }

    /**
     * {@inheritDoc}
     */
    public void setBatchResultHolder(BatchResultHolder batchResultHolder) {
        batchReport.setBatchResultHolder(batchResultHolder);
    }

    /**
     * {@inheritDoc}
     */
    public long getTotalInputRecordsNumber() {
        return batchReport.getTotalInputRecordsNumber();
    }

    /**
     * {@inheritDoc}
     */
    public long getRejectedRecordsNumber() {
        return batchReport.getRejectedRecordsNumber();
    }

    /**
     * {@inheritDoc}
     */
    public long getIgnoredRecordsNumber() {
        return batchReport.getIgnoredRecordsNumber();
    }

    /**
     * {@inheritDoc}
     */
    public long getProcessedRecordsNumber() {
        return batchReport.getProcessedRecordsNumber();
    }

    /**
     * {@inheritDoc}
     */
    public long getErrorRecordsNumber() {
        return batchReport.getErrorRecordsNumber();
    }

    /**
     * {@inheritDoc}
     */
    public long getStartTime() {
        return batchReport.getStartTime();
    }

    /**
     * {@inheritDoc}
     */
    public long getEndTime() {
        return batchReport.getEndTime();
    }

    /**
     * {@inheritDoc}
     */
    public BatchStatus getBatchStatus() {
        return batchStatus;
    }

    /**
     * {@inheritDoc}
     */
    public BatchResultHolder getBatchResultHolder() {
        return batchReport.getBatchResultHolder();
    }

    /**
     * {@inheritDoc}
     */
    public long getInputRecordsNumber() {
        return batchReport.getInputRecordsNumber();
    }

    /**
     * {@inheritDoc}
     */
    public BatchReport getBatchReport() {
        return batchReport;
    }

    /**
     * utility method to format error message
     * @param record the record in error
     * @param error the error returned by the engine
     * @param throwable the exception thrown
     * @param message the message to print
     * @return a formatted error message
     */
    private String formatError(final Record record, final String error, final Throwable throwable, final String message) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Record #")
                .append(record.getNumber())
                .append(" [")
                .append(record.getContentAsString())
                .append("] ")
                .append(message)
                .append(error);
        if (throwable != null) {
            StringWriter errors = new StringWriter();
            throwable.printStackTrace(new PrintWriter(errors));
            stringBuilder.append(errors.toString());
        }
        return stringBuilder.toString();
    }
}
