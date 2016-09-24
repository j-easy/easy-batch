/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.core.job;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.valueOf;
import static org.easybatch.core.job.JobParameters.DEFAULT_LIMIT;
import static org.easybatch.core.job.JobParameters.DEFAULT_TIMEOUT;
import static org.easybatch.core.util.Utils.toMinutes;

/**
 * Class holding job reporting data.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobReport implements Serializable {

    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String NOT_APPLICABLE = "N/A";

    private JobParameters parameters;

    private JobMetrics metrics;

    private JobStatus status;

    private JobResult result;

    public JobReport() {
        this.parameters = new JobParameters();
        this.metrics = new JobMetrics();
        this.status = JobStatus.STARTING;
    }

    public JobParameters getParameters() {
        return parameters;
    }

    public JobMetrics getMetrics() {
        return metrics;
    }

    @Deprecated
    public Object getResult() {
        return result == null ? null : result.get();
    }

    public void setJobResult(final JobResult result) {
        this.result = result;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    /*
     * Private utility methods
     */

    private float percent(final float current, final float total) {
        return (current * 100) / total;
    }

    private void appendPercent(final StringBuilder stringBuilder, final float percent) {
        stringBuilder.append(" (").append(percent).append("%)");
    }

    private String getFormattedMetric(final long metric) {
        final StringBuilder sb = new StringBuilder();
        sb.append(metric);
        Long totalCount = metrics.getTotalCount();
        if (totalCount != null && totalCount != 0) {
            appendPercent(sb, percent(metric, totalCount));
        }
        return sb.toString();
    }

    /*
     * Public utility methods to format report statistics
     */

    public String getFormattedStartTime() {
        return new SimpleDateFormat(DATE_FORMAT).format(new Date(metrics.getStartTime()));
    }

    public String getFormattedEndTime() {
        return new SimpleDateFormat(DATE_FORMAT).format(new Date(metrics.getEndTime()));
    }

    public String getFormattedDuration() {
        final StringBuilder sb = new StringBuilder();
        sb.append(metrics.getDuration()).append("ms");
        return sb.toString();
    }

    public String getFormattedDataSource() {
        String dataSource = parameters.getDataSource();
        return valueOf(dataSource == null ? NOT_APPLICABLE : dataSource);
    }

    public String getFormattedFilteredCount() {
        return getFormattedMetric(metrics.getFilteredCount());
    }

    public String getFormattedSkippedCount() {
        return getFormattedMetric(metrics.getSkippedCount());
    }

    public String getFormattedErrorCount() {
        return getFormattedMetric(metrics.getErrorCount());
    }

    public String getFormattedSuccessCount() {
        return getFormattedMetric(metrics.getSuccessCount());
    }

    public String getFormattedTotalCount() {
        Long totalCount = metrics.getTotalCount();
        return valueOf(totalCount == null ? NOT_APPLICABLE : totalCount);
    }

    public String getFormattedRecordProcessingTimeAverage() {
        Long totalCount = metrics.getTotalCount();
        if (totalCount == null || totalCount == 0) {
            return NOT_APPLICABLE;
        }
        return String.valueOf((float) metrics.getDuration() / (float) totalCount) + "ms";
    }

    // This is needed only for JMX
    public String getFormattedProgress() {
        Long totalCount = metrics.getTotalCount();
        if (totalCount == null || totalCount == 0) {
            return NOT_APPLICABLE;
        }
        long processedCount = metrics.getSkippedCount() + metrics.getFilteredCount() + metrics.getErrorCount() + metrics.getSuccessCount();
        String ratio = processedCount + "/" + totalCount;
        String percent = " (" + percent(processedCount, totalCount) + "%)";
        return ratio + percent;
    }

    public String getFormattedTimeout() {
        return parameters.getTimeout() != DEFAULT_TIMEOUT ? valueOf(toMinutes(parameters.getTimeout())) + "m" : NOT_APPLICABLE;
    }

    public String getFormattedLimit() {
        return parameters.getLimit() != DEFAULT_LIMIT ? valueOf(parameters.getLimit()) : NOT_APPLICABLE;
    }

    public String getFormattedResult() {
        return result != null && result.get() != null ? result.get().toString() : NOT_APPLICABLE;
    }

    @Override
    public String toString() {
        return new DefaultJobReportFormatter().formatReport(this);
    }

}
