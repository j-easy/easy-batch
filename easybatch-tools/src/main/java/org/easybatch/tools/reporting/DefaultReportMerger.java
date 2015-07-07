/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.tools.reporting;

import org.easybatch.core.api.Report;
import org.easybatch.core.api.Status;

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
 * <li>The total filtered records is the sum of total filtered records</li>
 * <li>The total ignored records is the sum of total ignored records</li>
 * <li>The total rejected records is the sum of total rejected records</li>
 * <li>The total error records is the sum of total error records</li>
 * <li>The total success records is the sum of total success records</li>
 * <li>The final processing times map is the merge of processing times maps</li>
 * <li>The final batch result is a list of all batch results</li>
 * <li>The final data source name is the concatenation (one per line) of data sources names</li>
 * <li>The final status is {@link org.easybatch.core.api.Status#FINISHED} (if all partials are finished)
 * or {@link org.easybatch.core.api.Status#ABORTED} (if one of partials is aborted).</li>
 * </ul>
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class DefaultReportMerger implements ReportMerger {

    /**
     * Merge multiple reports into a consolidated one.
     *
     * @param reports reports to merge
     * @return a merged report
     */
    @Override
    public Report mergerReports(Report... reports) {

        List<Long> startTimes = new ArrayList<Long>();
        List<Long> endTimes = new ArrayList<Long>();
        List<Object> results = new ArrayList<Object>();
        List<String> dataSources = new ArrayList<String>();
        long totalRecords = 0;

        Report finalReport = new Report(null);
        finalReport.setStatus(Status.FINISHED);

        for (Report report : reports) {
            startTimes.add(report.getStartTime());
            endTimes.add(report.getEndTime());
            totalRecords += report.getTotalRecords();
            calculateFilteredRecords(finalReport, report);
            calculateIgnoredRecords(finalReport, report);
            calculateRejectedRecords(finalReport, report);
            calculateErrorRecords(finalReport, report);
            calculateSuccessRecords(finalReport, report);
            addBatchResult(results, report);
            setStatus(finalReport, report);
            dataSources.add(report.getDataSource());
        }

        //merge results
        finalReport.setStartTime(Collections.min(startTimes));
        finalReport.setEndTime(Collections.max(endTimes));
        finalReport.setTotalRecords(totalRecords);

        mergeResults(results, finalReport);

        mergeDataSources(dataSources, finalReport);

        return finalReport;
    }

    private void mergeDataSources(List<String> dataSources, Report finalReport) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String dataSource : dataSources) {
            stringBuilder.append(dataSource).append(LINE_SEPARATOR);

        }
        finalReport.setDataSource(stringBuilder.toString());
    }

    private void mergeResults(List<Object> results, Report finalReport) {
        if (!results.isEmpty()) {
            finalReport.setBatchResult(results);
        }
    }

    private void setStatus(Report finalReport, Report report) {
        if (Status.ABORTED.equals(report.getStatus())) {
            finalReport.setStatus(Status.ABORTED);
        }
    }

    private void addBatchResult(List<Object> results, Report report) {
        if (report.getBatchResult() != null) {
            results.add(report.getBatchResult());
        }
    }

    private void calculateSuccessRecords(Report finalReport, Report report) {
        for (int i = 0; i < report.getSuccessRecordsCount(); i++) {
            finalReport.incrementTotalSuccessRecord();
        }
    }

    private void calculateErrorRecords(Report finalReport, Report report) {
        for (int i = 0; i < report.getErrorRecordsCount(); i++) {
            finalReport.incrementTotalErrorRecord();
        }
    }

    private void calculateRejectedRecords(Report finalReport, Report report) {
        for (int i = 0; i < report.getRejectedRecordsCount(); i++) {
            finalReport.incrementTotalRejectedRecord();
        }
    }

    private void calculateIgnoredRecords(Report finalReport, Report report) {
        for (int i = 0; i < report.getIgnoredRecordsCount(); i++) {
            finalReport.incrementTotalIgnoredRecord();
        }
    }

    private void calculateFilteredRecords(Report finalReport, Report report) {
        for (int i = 0; i < report.getFilteredRecordsCount(); i++) {
            finalReport.incrementTotalFilteredRecords();
        }
    }
}
