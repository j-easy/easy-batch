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
 *//**
 * Created by benas on 7/18/16.
 */

package org.easybatch.core.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

/**
 * A report merger that generates a merged report defined as follows:
 * <ul>
 * <li>The start time is the minimum of start times</li>
 * <li>The end time is the maximum of end times</li>
 * <li>The total records is the sum of total records</li>
 * <li>The total skipped records is the sum of total skipped records</li>
 * <li>The total filtered records is the sum of total filtered records</li>
 * <li>The total error records is the sum of total error records</li>
 * <li>The total success records is the sum of total success records</li>
 * <li>The final job result is a list of all job results</li>
 * <li>The final data source name is the concatenation (one per line) of data sources names</li>
 * <li>The final status is {@link JobStatus#COMPLETED} (if all partials are completed)
 * or {@link JobStatus#ABORTED} (if one of partials has been aborted) or {@link JobStatus#FAILED} (if one of partials has failed).</li>
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
        List<Object> results = new ArrayList<>();
        List<String> dataSources = new ArrayList<>();
        long totalRecords = 0;

        JobReport finalJobReport = new JobReport();
        finalJobReport.setStatus(JobStatus.COMPLETED);

        for (JobReport jobReport : jobReports) {
            startTimes.add(jobReport.getMetrics().getStartTime());
            endTimes.add(jobReport.getMetrics().getEndTime());
            totalRecords += jobReport.getMetrics().getTotalCount();
            calculateSkippedRecords(finalJobReport, jobReport);
            calculateFilteredRecords(finalJobReport, jobReport);
            calculateErrorRecords(finalJobReport, jobReport);
            calculateSuccessRecords(finalJobReport, jobReport);
            addJobResult(results, jobReport);
            setStatus(finalJobReport, jobReport);
            dataSources.add(jobReport.getParameters().getDataSource());
        }

        //merge results
        finalJobReport.getMetrics().setStartTime(Collections.min(startTimes));
        finalJobReport.getMetrics().setEndTime(Collections.max(endTimes));
        finalJobReport.getMetrics().setTotalCount(totalRecords);

        mergeResults(results, finalJobReport);

        mergeDataSources(dataSources, finalJobReport);

        return finalJobReport;
    }

    private void mergeDataSources(List<String> dataSources, JobReport finalJobReport) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String dataSource : dataSources) {
            stringBuilder.append(dataSource).append(LINE_SEPARATOR);

        }
        finalJobReport.getParameters().setDataSource(stringBuilder.toString());
    }

    private void mergeResults(List<Object> results, JobReport finalJobReport) {
        if (!results.isEmpty()) {
            finalJobReport.setJobResult(new JobResult(results));
        }
    }

    private void setStatus(JobReport finalJobReport, JobReport jobReport) {
        if (JobStatus.ABORTED.equals(jobReport.getStatus())) {
            finalJobReport.setStatus(JobStatus.ABORTED);
        }
        if (JobStatus.FAILED.equals(jobReport.getStatus())) {
            finalJobReport.setStatus(JobStatus.FAILED);
        }
    }

    private void addJobResult(List<Object> results, JobReport jobReport) {
        if (jobReport.getResult() != null) {
            results.add(jobReport.getResult());
        }
    }

    private void calculateSuccessRecords(JobReport finalJobReport, JobReport jobReport) {
        for (int i = 0; i < jobReport.getMetrics().getSuccessCount(); i++) {
            finalJobReport.getMetrics().incrementSuccessCount();
        }
    }

    private void calculateErrorRecords(JobReport finalJobReport, JobReport jobReport) {
        for (int i = 0; i < jobReport.getMetrics().getErrorCount(); i++) {
            finalJobReport.getMetrics().incrementErrorCount();
        }
    }

    private void calculateFilteredRecords(JobReport finalJobReport, JobReport jobReport) {
        for (int i = 0; i < jobReport.getMetrics().getFilteredCount(); i++) {
            finalJobReport.getMetrics().incrementFilteredCount();
        }
    }

    private void calculateSkippedRecords(JobReport finalJobReport, JobReport jobReport) {
        for (int i = 0; i < jobReport.getMetrics().getSkippedCount(); i++) {
            finalJobReport.getMetrics().incrementSkippedCount();
        }
    }
}
