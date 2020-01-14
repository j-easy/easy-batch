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

import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import static org.easybatch.core.util.Utils.*;

/**
 * Default job report formatter.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 * @deprecated This class is deprecated since v5.3 and will be removed in v6.
 */
@Deprecated
public class DefaultJobReportFormatter implements JobReportFormatter<String> {

    private String reportFormat =
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

    @Override
    public String formatReport(JobReport jobReport) {

        JobParameters parameters = jobReport.getParameters();
        JobMetrics metrics = jobReport.getMetrics();
        Map<String, Object> customMetrics = metrics.getCustomMetrics();
        Properties systemProperties = jobReport.getSystemProperties();

        String baseReport = MessageFormat.format(reportFormat,
                jobReport.getJobName(),
                jobReport.getStatus(),
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

        // for loops are not supported in MessageFormat (and were not designed to, unlike advanced template engines)

        // add custom metrics
        for (Map.Entry<String, Object> customMetric : customMetrics.entrySet()) {
            sb.append(LINE_SEPARATOR).append("\t").append(customMetric.getKey()).append(" = ").append(customMetric.getValue());
        }

        // add system properties
        sb.append(LINE_SEPARATOR).append("System properties:");
        for (Map.Entry property : new TreeMap<>(systemProperties).entrySet()) { // tree map for sorting purpose (repeatable tests)
            sb.append(LINE_SEPARATOR).append("\t").append(property.getKey()).append(" = ").append(property.getValue());
        }

        return sb.toString();
    }
}
