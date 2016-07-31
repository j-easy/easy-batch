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
package org.easybatch.tools.monitoring;

import org.easybatch.core.jmx.JobMonitoringListener;
import org.easybatch.core.job.JobReport;

import static java.lang.String.format;

/**
 * Support class to report job progress on the standard output.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class CliJobMonitoringListener extends JobMonitoringListener {

    @Override
    public void onJobReportUpdate(final JobReport jobReport) {
        System.out.print("\r" + format("Total count = %s | Skipped count = %s | Filtered count = %s | Error count = %s | Success count = %s",
                jobReport.getFormattedTotalCount(), jobReport.getFormattedSkippedCount(),
                jobReport.getFormattedFilteredCount(), jobReport.getFormattedErrorCount(),
                jobReport.getFormattedSuccessCount()));
    }

    @Override
    public void onConnectionClosed(final JobReport jobReport) {
        System.out.println();
        System.out.println(jobReport);
        System.exit(0);
    }

}
