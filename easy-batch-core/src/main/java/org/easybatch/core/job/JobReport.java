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
package org.easybatch.core.job;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import static org.easybatch.core.util.Utils.LINE_SEPARATOR;
import static org.easybatch.core.util.Utils.formatDuration;
import static org.easybatch.core.util.Utils.formatErrorThreshold;
import static org.easybatch.core.util.Utils.formatTime;

/**
 * Class holding job reporting data.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobReport implements Serializable {

    private static final String REPORT_FORMAT =
                    "Job Report:" + LINE_SEPARATOR +
                    "===========" + LINE_SEPARATOR +
                    "Name: {0}" + LINE_SEPARATOR +
                    "Status: {1}" + LINE_SEPARATOR +
                    "Parameters:" + LINE_SEPARATOR +
                    "\tBatch size = {2}" + LINE_SEPARATOR +
                    "\tError threshold = {3}" + LINE_SEPARATOR +
                    "\tJmx monitoring = {4}" + LINE_SEPARATOR +
                    "Metrics:" + LINE_SEPARATOR +
                    "\tStart time = {5}" + LINE_SEPARATOR +
                    "\tEnd time = {6}" + LINE_SEPARATOR +
                    "\tDuration = {7}" + LINE_SEPARATOR +
                    "\tRead count = {8}" + LINE_SEPARATOR +
                    "\tWrite count = {9}" + LINE_SEPARATOR +
                    "\tFilter count = {10}" + LINE_SEPARATOR +
                    "\tError count = {11}";

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
                formatErrorThreshold(parameters.getErrorThreshold()),
                parameters.isJmxMonitoring(),
                formatTime(metrics.getStartTime()),
                formatTime(metrics.getEndTime()),
                formatDuration(metrics.getDuration()),
                metrics.getReadCount(),
                metrics.getWriteCount(),
                metrics.getFilterCount(),
                metrics.getErrorCount());

        final StringBuilder sb = new StringBuilder(baseReport);
        // append custom metrics
        for (Map.Entry<String, Object> customMetric : metrics.getCustomMetrics().entrySet()) {
            sb.append(LINE_SEPARATOR).append("\t")
                    .append(customMetric.getKey()).append(" = ").append(customMetric.getValue());
        }
        // append system properties
        sb.append(LINE_SEPARATOR).append("System properties:");
        for (Map.Entry property : new TreeMap<>(systemProperties).entrySet()) { // tree map for sorting purpose (repeatable tests)
            sb.append(LINE_SEPARATOR).append("\t")
                    .append(property.getKey()).append(" = ").append(property.getValue());
        }
        return sb.toString();
    }
}
