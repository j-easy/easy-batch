/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import static org.easybatch.core.util.Utils.*;

/**
 * Default job report formatter.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class DefaultJobReportFormatter implements JobReportFormatter<String> {

    @Override
    public String formatReport(JobReport jobReport) {
        final StringBuilder sb = new StringBuilder("Job Report:");
        sb.append(LINE_SEPARATOR).append("===========");

        /*
         * Job status
         */
        sb.append(LINE_SEPARATOR).append("Name: ").append(jobReport.getJobName());
        sb.append(LINE_SEPARATOR).append("Status: ").append(jobReport.getStatus());

        /*
         * Job parameters
         */
        JobParameters parameters = jobReport.getParameters();
        sb.append(LINE_SEPARATOR).append("Parameters:");
        sb.append(LINE_SEPARATOR).append("\tBatch size = ").append(parameters.getBatchSize());
        sb.append(LINE_SEPARATOR).append("\tError threshold = ").append(formatErrorThreshold(parameters.getErrorThreshold()));
        sb.append(LINE_SEPARATOR).append("\tJmx monitoring = ").append(parameters.isJmxMonitoring());

        /*
         * Job metrics
         */
        JobMetrics metrics = jobReport.getMetrics();
        sb.append(LINE_SEPARATOR).append("Metrics:");
        sb.append(LINE_SEPARATOR).append("\tStart time = ").append(formatTime(metrics.getStartTime()));
        sb.append(LINE_SEPARATOR).append("\tEnd time = ").append(formatTime(metrics.getEndTime()));
        sb.append(LINE_SEPARATOR).append("\tDuration = ").append(formatDuration(metrics.getDuration()));
        sb.append(LINE_SEPARATOR).append("\tRead count = ").append(metrics.getReadCount());
        sb.append(LINE_SEPARATOR).append("\tWrite count = ").append(metrics.getWriteCount());
        sb.append(LINE_SEPARATOR).append("\tFiltered count = ").append(metrics.getFilteredCount());
        sb.append(LINE_SEPARATOR).append("\tError count = ").append(metrics.getErrorCount());

        return sb.toString();
    }
}
