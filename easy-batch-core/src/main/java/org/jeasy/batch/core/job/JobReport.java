/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.jeasy.batch.core.job;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

import org.jeasy.batch.core.util.Utils;

/**
 * Class holding job reporting data.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobReport implements Serializable {

    private static final String REPORT_FORMAT =
                    "Job Report:" + Utils.LINE_SEPARATOR +
                    "===========" + Utils.LINE_SEPARATOR +
                    "Name: {0}" + Utils.LINE_SEPARATOR +
                    "Status: {1}" + Utils.LINE_SEPARATOR +
                    "Parameters:" + Utils.LINE_SEPARATOR +
                    "\tBatch size = {2}" + Utils.LINE_SEPARATOR +
                    "\tError threshold = {3}" + Utils.LINE_SEPARATOR +
                    "\tJmx monitoring = {4}" + Utils.LINE_SEPARATOR +
                    "\tBatch scanning = {5}" + Utils.LINE_SEPARATOR +
                    "Metrics:" + Utils.LINE_SEPARATOR +
                    "\tStart time = {6}" + Utils.LINE_SEPARATOR +
                    "\tEnd time = {7}" + Utils.LINE_SEPARATOR +
                    "\tDuration = {8}" + Utils.LINE_SEPARATOR +
                    "\tRead count = {9}" + Utils.LINE_SEPARATOR +
                    "\tWrite count = {10}" + Utils.LINE_SEPARATOR +
                    "\tFilter count = {11}" + Utils.LINE_SEPARATOR +
                    "\tError count = {12}";

    private String jobName;

    private JobParameters parameters;

    private JobMetrics metrics;

    private JobStatus status;

    private Throwable lastError;

    private Properties systemProperties;

    public String getJobName() {
        return jobName;
    }

    public JobParameters getParameters() {
        return parameters;
    }

    public JobMetrics getMetrics() {
        return metrics;
    }

    public JobStatus getStatus() {
        return status;
    }

    public Throwable getLastError() {
        return lastError;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setParameters(JobParameters parameters) {
        this.parameters = parameters;
    }

    public void setMetrics(JobMetrics metrics) {
        this.metrics = metrics;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public void setLastError(Throwable lastError) {
        this.lastError = lastError;
    }

    public Properties getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(Properties systemProperties) {
        this.systemProperties = systemProperties;
    }

    @Override
    public String toString() {
        String baseReport = MessageFormat.format(REPORT_FORMAT,
                jobName,
                status,
                parameters.getBatchSize(),
                Utils.formatErrorThreshold(parameters.getErrorThreshold()),
                parameters.isJmxMonitoring(),
                parameters.isBatchScanningEnabled(),
                Utils.formatTime(metrics.getStartTime()),
                Utils.formatTime(metrics.getEndTime()),
                Utils.formatDuration(metrics.getDuration()),
                metrics.getReadCount(),
                metrics.getWriteCount(),
                metrics.getFilterCount(),
                metrics.getErrorCount());

        final StringBuilder sb = new StringBuilder(baseReport);
        // append custom metrics
        for (Map.Entry<String, Object> customMetric : metrics.getCustomMetrics().entrySet()) {
            sb.append(Utils.LINE_SEPARATOR).append("\t")
                    .append(customMetric.getKey()).append(" = ").append(customMetric.getValue());
        }
        return sb.toString();
    }
}
