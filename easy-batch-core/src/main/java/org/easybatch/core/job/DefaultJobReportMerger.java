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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A report merger that generates a merged report defined as follows:
 * <ul>
 * <li>The start time is the minimum of start times</li>
 * <li>The end time is the maximum of end times</li>
 * <li>The total read records is the sum of total read records</li>
 * <li>The total written records is the sum of total written records</li>
 * <li>The total filtered records is the sum of total filtered records</li>
 * <li>The total error records is the sum of total error records</li>
 * <li>The final status is {@link JobStatus#COMPLETED} (if all partials are completed) or {@link JobStatus#FAILED} (if one of partials has failed).</li>
 * <li>The final name is the concatenation of partial job names.</li>
 * </ul>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class DefaultJobReportMerger implements JobReportMerger {

    /**
     * Merge multiple reports into a consolidated one.
     *
     * @param jobReports reports to merge
     * @return a merged report
     */
    @Override
    public JobReport mergerReports(JobReport... jobReports) {

        List<Long> startTimes = new ArrayList<>();
        List<Long> endTimes = new ArrayList<>();
        List<String> jobNames = new ArrayList<>();

        JobParameters parameters = new JobParameters();
        JobMetrics metrics = new JobMetrics();
        JobReport finalJobReport = new JobReport();
        finalJobReport.setParameters(parameters);
        finalJobReport.setMetrics(metrics);
        finalJobReport.setStatus(JobStatus.COMPLETED);

        for (JobReport jobReport : jobReports) {
            startTimes.add(jobReport.getMetrics().getStartTime());
            endTimes.add(jobReport.getMetrics().getEndTime());
            calculateReadRecords(finalJobReport, jobReport);
            calculateWrittenRecords(finalJobReport, jobReport);
            calculateFilteredRecords(finalJobReport, jobReport);
            calculateErrorRecords(finalJobReport, jobReport);
            setStatus(finalJobReport, jobReport);
            jobNames.add(jobReport.getJobName());
            finalJobReport.setSystemProperties(jobReport.getSystemProperties()); // works unless partial jobs are run in different JVMs..
        }

        //merge results
        finalJobReport.getMetrics().setStartTime(Collections.min(startTimes));
        finalJobReport.getMetrics().setEndTime(Collections.max(endTimes));

        // set name
        finalJobReport.setJobName(concatenate(jobNames));

        return finalJobReport;
    }

    private void setStatus(JobReport finalJobReport, JobReport jobReport) {
        if (JobStatus.FAILED.equals(jobReport.getStatus())) {
            finalJobReport.setStatus(JobStatus.FAILED);
        }
    }

    private void calculateReadRecords(JobReport finalJobReport, JobReport jobReport) {
        finalJobReport.getMetrics().incrementReadCount(jobReport.getMetrics().getReadCount());
    }

    private void calculateWrittenRecords(JobReport finalJobReport, JobReport jobReport) {
        finalJobReport.getMetrics().incrementWriteCount(jobReport.getMetrics().getWriteCount());
    }

    private void calculateErrorRecords(JobReport finalJobReport, JobReport jobReport) {
        finalJobReport.getMetrics().incrementErrorCount(jobReport.getMetrics().getErrorCount());
    }

    private void calculateFilteredRecords(JobReport finalJobReport, JobReport jobReport) {
        finalJobReport.getMetrics().incrementFilterCount(jobReport.getMetrics().getFilterCount());
    }

    private String concatenate(List<String> names) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = names.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next());
            if (iterator.hasNext()) {
                stringBuilder.append("|");
            }
        }
        return stringBuilder.toString();
    }

}
