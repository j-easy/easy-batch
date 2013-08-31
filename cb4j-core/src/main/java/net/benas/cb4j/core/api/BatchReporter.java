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

package net.benas.cb4j.core.api;

import net.benas.cb4j.core.model.Record;
import net.benas.cb4j.core.util.BatchStatus;

/**
 * Utility service used to report ignored/rejected/error records and generate batch report.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public interface BatchReporter {

    /**
     * report a rejected record.
     * @param record the record to reject
     * @param error the error that causes the record to be rejected
     */
    void reportRejectedRecord(Record record, String error);

    /**
     * report a rejected record.
     * @param record the record to reject
     * @param error the error message
     * @param throwable the thrown exception that causes the record to be rejected
     */
    void reportRejectedRecord(Record record, String error, Throwable throwable);

    /**
     * report an ignored record.
     * @param record the record to ignore
     * @param recordNumber the record number
     * @param error the error that causes the record to be ignored
     */
    void reportIgnoredRecord(String record, long recordNumber, String error);

    /**
     * report a record processed with error.
     * @param record the record processed with error
     * @param error the cause error
     */
    void reportErrorRecord(Record record, String error);

    /**
     * report a record processed with error.
     * @param record the record processed with error
     * @param error the cause error
     * @param throwable the thrown exception that causes the record to be rejected
     */
    void reportErrorRecord(Record record, String error, Throwable throwable);

    /**
     * Initialize the batch reporter.
     */
    void init();

    /**
     * generate a batch execution report.
     */
    void generateReport();

    /**
     * Set the total input records number.
     * @param totalInputRecordsNumber the total input records number
     */
    void setTotalInputRecordsNumber(long totalInputRecordsNumber);

    /**
     * Set input records number.
     * @param inputRecordsNumber input records number
     */
    void setInputRecordsNumber(long inputRecordsNumber);

    /**
     * Set processed records number.
     * @param processedRecordsNumber processed records number
     */
    void setProcessedRecordsNumber(long processedRecordsNumber);

    /**
     * set the batch execution start time.
     * @param startTime the batch execution start time
     */
    void setStartTime(long startTime);

    /**
     * set the batch execution end time.
     * @param endTime the batch execution end time
     */
    void setEndTime(long endTime);

    /**
     * set the batch execution status.
     * @param batchStatus the batch status
     */
    void setBatchStatus(BatchStatus batchStatus);

    /**
     * set the batch execution result holder.
     * @param batchResultHolder batch execution result holder
     */
    void setBatchResultHolder(BatchResultHolder<?> batchResultHolder);

    /**
     * set total input records number.
     * @return total input records number
     */
    long getTotalInputRecordsNumber();

    /**
     * get input records number.
     * @return input records number
     */
    long getInputRecordsNumber();

    /**
     * get rejected records number.
     * @return rejected records number
     */
    long getRejectedRecordsNumber();

    /**
     * get ignored records number.
     * @return ignored records number
     */
    long getIgnoredRecordsNumber();

    /**
     * get processed records number.
     * @return processed records number
     */
    long getProcessedRecordsNumber();

    /**
     * get error records number.
     * @return error records number
     */
    long getErrorRecordsNumber();

    /**
     * get the batch execution start time.
     * @return batch execution start time.
     */
    long getStartTime();

    /**
     * get the batch execution end time.
     * @return batch execution end time.
     */
    long getEndTime();

    /**
     * get the batch execution status.
     * @return batch execution status
     */
    BatchStatus getBatchStatus();

    /**
     * get batch execution result holder.
     * @return batch execution result holder
     */
    BatchResultHolder<?> getBatchResultHolder();

    /**
     * get the batch execution report.
     * @return the batch execution report
     */
    BatchReport getBatchReport();

}
