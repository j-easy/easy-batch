/*
 * The MIT License
 *
 *  Copyright (c) 2013, benas (md.benhassine@gmail.com)
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

package io.github.benas.cb4j.core.api;

import java.io.Serializable;

/**
 * Class holding batch execution report.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class BatchReport implements Serializable {

    /**
     * Total input records number.
     */
    private long totalInputRecordsNumber;

    /**
     * Input records number.
     */
    private long inputRecordsNumber;

    /**
     * Rejected records number.
     */
    private long rejectedRecordsNumber;

    /**
     * Ignored records number.
     */
    private long ignoredRecordsNumber;

    /**
     * Processed records number.
     */
    private long processedRecordsNumber;

    /**
     * Error records number.
     */
    private long errorRecordsNumber;

    /**
     * Batch execution start time.
     */
    private long startTime;

    /**
     * Batch execution end time.
     */
    private long endTime;

    /**
     * Batch execution result holder.
     */
    private BatchResultHolder<?> batchResultHolder;

    /*
     * Getters and setters
     */

    public long getTotalInputRecordsNumber() {
        return totalInputRecordsNumber;
    }

    public void setTotalInputRecordsNumber(long totalInputRecordsNumber) {
        this.totalInputRecordsNumber = totalInputRecordsNumber;
    }

    public long getInputRecordsNumber() {
        return inputRecordsNumber;
    }

    public void setInputRecordsNumber(long inputRecordsNumber) {
        this.inputRecordsNumber = inputRecordsNumber;
    }

    public long getRejectedRecordsNumber() {
        return rejectedRecordsNumber;
    }

    public void setRejectedRecordsNumber(long rejectedRecordsNumber) {
        this.rejectedRecordsNumber = rejectedRecordsNumber;
    }

    public long getIgnoredRecordsNumber() {
        return ignoredRecordsNumber;
    }

    public void setIgnoredRecordsNumber(long ignoredRecordsNumber) {
        this.ignoredRecordsNumber = ignoredRecordsNumber;
    }

    public long getProcessedRecordsNumber() {
        return processedRecordsNumber;
    }

    public void setProcessedRecordsNumber(long processedRecordsNumber) {
        this.processedRecordsNumber = processedRecordsNumber;
    }

    public long getErrorRecordsNumber() {
        return errorRecordsNumber;
    }

    public void setErrorRecordsNumber(long errorRecordsNumber) {
        this.errorRecordsNumber = errorRecordsNumber;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public BatchResultHolder<?> getBatchResultHolder() {
        return batchResultHolder;
    }

    public void setBatchResultHolder(BatchResultHolder<?> batchResultHolder) {
        this.batchResultHolder = batchResultHolder;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BatchReport");
        sb.append("{startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", totalInputRecordsNumber=").append(totalInputRecordsNumber);
        sb.append(", inputRecordsNumber=").append(inputRecordsNumber);
        sb.append(", rejectedRecordsNumber=").append(rejectedRecordsNumber);
        sb.append(", ignoredRecordsNumber=").append(ignoredRecordsNumber);
        sb.append(", processedRecordsNumber=").append(processedRecordsNumber);
        sb.append(", errorRecordsNumber=").append(errorRecordsNumber);
        sb.append(", batchResultHolder=").append(batchResultHolder);
        sb.append('}');
        return sb.toString();
    }
}
