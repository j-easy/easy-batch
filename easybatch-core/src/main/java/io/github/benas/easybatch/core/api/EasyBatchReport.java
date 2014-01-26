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

package io.github.benas.easybatch.core.api;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A class holding easy batch reporting data.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class EasyBatchReport implements Serializable {

    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    private long startTime;

    private long endTime;

    private String dataSource;

    private long totalRecords;

    // needed only for jmx
    private long currentRecordNumber;

    private List<Long> filteredRecords;

    private List<Long> ignoredRecords;

    private List<Long> rejectedRecords;

    private List<Long> errorRecords;

    private Map<Long, Long> processingTimes;

    private Object easyBatchResult;

    public EasyBatchReport() {
        filteredRecords = new ArrayList<Long>();
        ignoredRecords = new ArrayList<Long>();
        rejectedRecords = new ArrayList<Long>();
        errorRecords = new ArrayList<Long>();
        processingTimes = new HashMap<Long, Long>();
    }

    public void addFilteredRecord(long recordNumber) {
        filteredRecords.add(recordNumber);
    }

    public void addIgnoredRecord(final long recordNumber) {
        ignoredRecords.add(recordNumber);
    }

    public void addRejectedRecord(final long recordNumber) {
        rejectedRecords.add(recordNumber);
    }

    public void addErrorRecord(final long recordNumber) {
        errorRecords.add(recordNumber);
    }

    public void addProcessingTime(long recordNumber, long processingTime) {
        processingTimes.put(recordNumber, processingTime);
    }

    public void setTotalRecords(final long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public List<Long> getFilteredRecords() {
        return filteredRecords;
    }

    public List<Long> getIgnoredRecords() {
        return ignoredRecords;
    }

    public List<Long> getRejectedRecords() {
        return rejectedRecords;
    }

    public List<Long> getErrorRecords() {
        return errorRecords;
    }

    public Map<Long, Long> getProcessingTimes() {
        return processingTimes;
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

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public long getCurrentRecordNumber() {
        return currentRecordNumber;
    }

    public void setCurrentRecordNumber(long currentRecordNumber) {
        this.currentRecordNumber = currentRecordNumber;
    }

    public Object getEasyBatchResult() {
        return easyBatchResult;
    }

    public void setEasyBatchResult(Object easyBatchResult) {
        this.easyBatchResult = easyBatchResult;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Easy Batch Report:");
        sb.append("\n\tStart time = ").append(getFormattedStartTime());
        sb.append("\n\tEnd time = ").append(getFormattedEndTime());
        sb.append("\n\tBatch duration = ").append(getFormattedBatchDuration());
        sb.append("\n\tData source = ").append(dataSource);
        sb.append("\n\tTotal records = ").append(totalRecords);
        sb.append("\n\tFiltered records = ").append(getFormattedFilteredRecords()).append(filteredRecords);
        sb.append("\n\tIgnored records = ").append(getFormattedIgnoredRecords()).append(ignoredRecords);
        sb.append("\n\tRejected records = ").append(getFormattedRejectedRecords()).append(rejectedRecords);
        sb.append("\n\tError records = ").append(getFormattedErrorRecords()).append(errorRecords);
        sb.append("\n\tProcessed records = ").append(getFormattedProcessedRecords());
        sb.append("\n\tAverage record processing time = ").append(getFormattedAverageRecordProcessingTime());
        sb.append("\n\tResult = ").append(easyBatchResult);
        return sb.toString();
    }

    /**
     * Calculate a percent of a value regarding to a total.
     * @param current the value
     * @param total the total
     * @return the percent.
     */
    public int percent(final long current, final long total) {
        return (int) (((float) current / (float) total) * 100);
    }

    public int getFilteredRecordsNumber() {
        return filteredRecords.size();
    }

    public int getIgnoredRecordsNumber() {
        return ignoredRecords.size();
    }

    public int getRejectedRecordsNumber() {
        return rejectedRecords.size();
    }

    public int getErrorRecordsNumber() {
        return errorRecords.size();
    }

    public long getProcessedRecordsNumber() {
        return totalRecords - getFilteredRecordsNumber() - getIgnoredRecordsNumber() - getRejectedRecordsNumber() - getErrorRecordsNumber();
    }

    public long getFilteredRecordsPercent() {
        return percent(getFilteredRecordsNumber(), totalRecords);
    }

    public long getIgnoredRecordsPercent() {
        return percent(getIgnoredRecordsNumber(), totalRecords);
    }

    public long getRejectedRecordsPercent() {
        return percent(getRejectedRecordsNumber(), totalRecords);
    }

    public long getErrorRecordsPercent() {
        return percent(getErrorRecordsNumber(), totalRecords);
    }

    public long getProcessedRecordsPercent() {
        return percent(getProcessedRecordsNumber(), totalRecords);
    }

    public long getBatchDuration() {
        return (endTime - startTime) / 1000;
    }

    public String getFormattedBatchDuration() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getBatchDuration()).append("s");
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
        sb.append(getFilteredRecordsNumber()).append(" (").append(getFilteredRecordsPercent()).append("%) ");
        return sb.toString();
    }

    public String getFormattedIgnoredRecords() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getIgnoredRecordsNumber()).append(" (").append(getIgnoredRecordsPercent()).append("%) ");
        return sb.toString();
    }

    public String getFormattedRejectedRecords() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getRejectedRecordsNumber()).append(" (").append(getRejectedRecordsPercent()).append("%) ");
        return sb.toString();
    }

    public String getFormattedErrorRecords() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getErrorRecordsNumber()).append(" (").append(getErrorRecordsPercent()).append("%) ");
        return sb.toString();
    }

    public String getFormattedProcessedRecords() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getProcessedRecordsNumber()).append(" (").append(getProcessedRecordsPercent()).append("%) ");
        return sb.toString();
    }

    public String getFormattedAverageRecordProcessingTime() {
        final StringBuilder sb = new StringBuilder();
        sb.append( (float) getBatchDuration() / (float) totalRecords).append("s");
        return sb.toString();
    }

    // This is needed only for JMX
    public String getFormattedCurrentlyProcessedRecords() {
        long currentlyProcessedRecords = getCurrentRecordNumber() - 1 - getFilteredRecordsNumber() - getErrorRecordsNumber() - getIgnoredRecordsNumber() - getRejectedRecordsNumber();
        final StringBuilder sb = new StringBuilder();
        sb.append(currentlyProcessedRecords).append(" (").append(percent(currentlyProcessedRecords, totalRecords)).append("%) ");
        return sb.toString();
    }

    // This is needed only for JMX
    public String getFormattedProgress() {
        String ratio = currentRecordNumber + "/" + totalRecords;
        String percent = " (" + percent(currentRecordNumber, totalRecords) + "%)";
        return ratio + percent;
    }

}
