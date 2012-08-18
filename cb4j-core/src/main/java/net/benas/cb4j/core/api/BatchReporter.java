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

/**
 * Utility service used to ignore/reject records and generate batch report
 *
 * @author benas (md.benhassine@gmail.com)
 */
public interface BatchReporter {

    /**
     * reject a record
     * @param record the record to reject
     * @param error the error that causes the record to be rejected
     */
    void rejectRecord(Record record, String error);

    /**
     * ignore a record
     * @param record the record to ignore
     * @param recordNumber the record number
     * @param recordSize the record size
     */
    void ignoreRecord(String record, long recordNumber, int recordSize);

    /**
     * generate a batch execution report
     */
    void generateReport();

    /**
     * Set the total input records
     * @param totalInputRecords the total input records
     */
    void setTotalInputRecords(long totalInputRecords);

    /**
     * set the batch execution start time
     * @param startTime the batch execution start time
     */
    public void setStartTime(long startTime);

    /**
     * set the batch execution end time
     * @param endTime the batch execution end time
     */
    public void setEndTime(long endTime);
}
