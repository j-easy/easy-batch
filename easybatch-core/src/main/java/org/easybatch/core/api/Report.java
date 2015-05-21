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

package org.easybatch.core.api;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class holding batch reporting data.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class Report implements Serializable {

    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    private long startTime;

    private long endTime;

    private String dataSource;

    private Long totalRecords;

    // needed only for jmx monitoring
    private long currentRecordNumber;

    private long filteredRecords;

    private long ignoredRecords;

    private long rejectedRecords;

    private long errorRecords;

    private long successRecords;

    private Object batchResult;

    private Status status;

    public Report() {
        status = Status.INITIALIZING;
    }

    public void incrementTotalFilteredRecords() {
        filteredRecords++;
    }

    public void incrementTotalIgnoredRecord() {
        ignoredRecords++;
    }

    public void incrementTotalRejectedRecord() {
        rejectedRecords++;
    }

    public void incrementTotalErrorRecord() {
        errorRecords++;
    }

    public void incrementTotalSuccessRecord() {
        successRecords++;
    }

    public void setTotalRecords(final Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(final long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(final long endTime) {
        this.endTime = endTime;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(final String dataSource) {
        this.dataSource = dataSource;
    }

    public long getCurrentRecordNumber() {
        return currentRecordNumber;
    }

    public void setCurrentRecordNumber(final long currentRecordNumber) {
        this.currentRecordNumber = currentRecordNumber;
    }

    public Object getBatchResult() {
        return batchResult;
    }

    public void setBatchResult(final Object batchResult) {
        this.batchResult = batchResult;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getFilteredRecordsCount() {
        return filteredRecords;
    }

    public long getIgnoredRecordsCount() {
        return ignoredRecords;
    }

    public long getRejectedRecordsCount() {
        return rejectedRecords;
    }

    public long getErrorRecordsCount() {
        return errorRecords;
    }

    public long getSuccessRecordsCount() {
        return successRecords;
    }

    /*
     * Private utility methods
     */

    private float percent(final float current, final float total) {
        return (current / total) * 100;
    }

    private float getFilteredRecordsPercent() {
        return percent(getFilteredRecordsCount(), totalRecords);
    }

    private float getIgnoredRecordsPercent() {
        return percent(getIgnoredRecordsCount(), totalRecords);
    }

    private float getRejectedRecordsPercent() {
        return percent(getRejectedRecordsCount(), totalRecords);
    }

    private float getErrorRecordsPercent() {
        return percent(getErrorRecordsCount(), totalRecords);
    }

    private float getSuccessRecordsPercent() {
        return percent(getSuccessRecordsCount(), totalRecords);
    }

    private long getBatchDuration() {
        return endTime - startTime;
    }

    /*
     * Public utility methods to format report statistics
     */

    public String getFormattedBatchDuration() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getBatchDuration()).append("ms");
        return sb.toString();
    }

    public String getFormattedEndTime() {
        return new SimpleDateFormat(DATE_FORMAT).format(new Date(endTime));
    }

    public String getFormattedStartTime() {
        return new SimpleDateFormat(DATE_FORMAT).format(new Date(startTime));
    }

    public String getFormattedFilteredRecords() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getFilteredRecordsCount());
        if (totalRecords != null) {
            sb.append(" (").append(getFilteredRecordsPercent()).append("%)");
        }
        return sb.toString();
    }

    public String getFormattedIgnoredRecords() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getIgnoredRecordsCount());
        if (totalRecords != null) {
            sb.append(" (").append(getIgnoredRecordsPercent()).append("%)");
        }
        return sb.toString();
    }

    public String getFormattedRejectedRecords() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getRejectedRecordsCount());
        if (totalRecords != null) {
            sb.append(" (").append(getRejectedRecordsPercent()).append("%)");
        }
        return sb.toString();
    }

    public String getFormattedErrorRecords() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getErrorRecordsCount());
        if (totalRecords != null) {
            sb.append(" (").append(getErrorRecordsPercent()).append("%)");
        }
        return sb.toString();
    }

    public String getFormattedSuccessRecords() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getSuccessRecordsCount());
        if (totalRecords != null) {
            sb.append(" (").append(getSuccessRecordsPercent()).append("%)");
        }
        return sb.toString();
    }

    public String getFormattedAverageRecordProcessingTime() {
        if (totalRecords == null || totalRecords == 0) {
            return "N/A";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append((float) getBatchDuration() / (float) totalRecords).append("ms");
        return sb.toString();
    }

    // This is needed only for JMX
    public String getFormattedProgress() {
        if (totalRecords == null || totalRecords == 0) {
            return "N/A";
        }
        String ratio = currentRecordNumber + "/" + totalRecords;
        String percent = " (" + percent(currentRecordNumber, totalRecords) + "%)";
        return ratio + percent;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Batch Report:");
        sb.append("\n\tStart time = ").append(getFormattedStartTime());
        sb.append("\n\tEnd time = ").append(getFormattedEndTime());
        sb.append("\n\tStatus = ").append(status);
        sb.append("\n\tBatch duration = ").append(getFormattedBatchDuration());
        sb.append("\n\tData source = ").append(dataSource);
        sb.append("\n\tTotal records = ").append(totalRecords == null ? "N/A" : totalRecords);
        sb.append("\n\tFiltered records = ").append(getFormattedFilteredRecords());
        sb.append("\n\tIgnored records = ").append(getFormattedIgnoredRecords());
        sb.append("\n\tRejected records = ").append(getFormattedRejectedRecords());
        sb.append("\n\tError records = ").append(getFormattedErrorRecords());
        sb.append("\n\tSuccess records = ").append(getFormattedSuccessRecords());
        sb.append("\n\tRecord processing time average = ").append(getFormattedAverageRecordProcessingTime());
        if (batchResult != null) {
            sb.append("\n\tResult = ").append(batchResult);
        }
        return sb.toString();
    }

}
