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

import static org.easybatch.core.job.JobParameters.DEFAULT_ERROR_THRESHOLD;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

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
        sb.append(LINE_SEPARATOR).append("Status: ").append(jobReport.getStatus());

        /*
         * Job parameters
         */
        JobParameters parameters = jobReport.getParameters();
        sb.append(LINE_SEPARATOR).append("Parameters:");
        sb.append(LINE_SEPARATOR).append("\tName = ").append(parameters.getName());
        sb.append(LINE_SEPARATOR).append("\tExecution Id = ").append(parameters.getExecutionId());
        sb.append(LINE_SEPARATOR).append("\tHost name = ").append(parameters.getHostname());
        sb.append(LINE_SEPARATOR).append("\tData source = ").append(jobReport.getFormattedDataSource());
        sb.append(LINE_SEPARATOR).append("\tReader retry policy = ").append(parameters.getRetryPolicy());
        sb.append(LINE_SEPARATOR).append("\tReader keep alive = ").append(parameters.isKeepAlive());
        sb.append(LINE_SEPARATOR).append("\tSkip = ").append(parameters.getSkip());
        sb.append(LINE_SEPARATOR).append("\tLimit = ").append(jobReport.getFormattedLimit());
        sb.append(LINE_SEPARATOR).append("\tTimeout = ").append(jobReport.getFormattedTimeout());
        long errorThreshold = parameters.getErrorThreshold();
        sb.append(LINE_SEPARATOR).append("\tError threshold = ").append(errorThreshold != DEFAULT_ERROR_THRESHOLD ? errorThreshold : "N/A");
        sb.append(LINE_SEPARATOR).append("\tSilent mode = ").append(parameters.isSilentMode());
        sb.append(LINE_SEPARATOR).append("\tJmx monitoring = ").append(parameters.isJmxMonitoring());

        /*
         * Job metrics
         */
        sb.append(LINE_SEPARATOR).append("Metrics:");
        sb.append(LINE_SEPARATOR).append("\tStart time = ").append(jobReport.getFormattedStartTime());
        sb.append(LINE_SEPARATOR).append("\tEnd time = ").append(jobReport.getFormattedEndTime());
        sb.append(LINE_SEPARATOR).append("\tDuration = ").append(jobReport.getFormattedDuration());
        sb.append(LINE_SEPARATOR).append("\tTotal count = ").append(jobReport.getFormattedTotalCount());
        sb.append(LINE_SEPARATOR).append("\tSkipped count = ").append(jobReport.getFormattedSkippedCount());
        sb.append(LINE_SEPARATOR).append("\tFiltered count = ").append(jobReport.getFormattedFilteredCount());
        sb.append(LINE_SEPARATOR).append("\tError count = ").append(jobReport.getFormattedErrorCount());
        sb.append(LINE_SEPARATOR).append("\tSuccess count = ").append(jobReport.getFormattedSuccessCount());
        sb.append(LINE_SEPARATOR).append("\tRecord processing time average = ").append(jobReport.getFormattedRecordProcessingTimeAverage());

        /*
         * Job result (if any)
         */
        sb.append(LINE_SEPARATOR).append("Result: ").append(jobReport.getFormattedResult());

        return sb.toString();
    }
}
