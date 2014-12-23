package org.easybatch.tools.reporting;

import org.easybatch.core.api.Report;
import org.easybatch.core.api.Status;

import java.util.*;

/**
 * A report aggregator that generate a merged report defined as follows:
 * <ul>
 *     <li>The start time is the minimum of start times</li>
 *     <li>The end time is the maximum of end times</li>
 *     <li>The total records is the sum of total records</li>
 *     <li>The total filtered records is the sum of total filtered records</li>
 *     <li>The total ignored records is the sum of total ignored records</li>
 *     <li>The total rejected records is the sum of total rejected records</li>
 *     <li>The total error records is the sum of total error records</li>
 *     <li>The total success records is the sum of total success records</li>
 *     <li>The final processing times map is the merge of processing times maps</li>
 *     <li>The final batch result is a list of all batch results</li>
 *     <li>The final data source name is the concatenation (one per line) of data sources names</li>
 *     <li>The final status is {@link org.easybatch.core.api.Status#FINISHED} (if all partials are finished)
 *          or {@link org.easybatch.core.api.Status#ABORTED} (if one of partials is aborted).</li>
 * </ul>
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class DefaultReportsAggregator implements ReportsAggregator {

    /**
     * Merge multiple Easy Batch reports into a consolidated one.
     * @param easyBatchReports reports to merge
     * @return a merged report
     */
    public Report aggregateReports(Report... easyBatchReports) {

        List<Long> startTimes = new ArrayList<Long>();

        List<Long> endTimes = new ArrayList<Long>();

        int totalRecords = 0;

        List<Integer> filteredRecords = new ArrayList<Integer>();

        List<Integer> ignoredRecords = new ArrayList<Integer>();

        List<Integer> rejectedRecords = new ArrayList<Integer>();

        List<Integer> errorRecords = new ArrayList<Integer>();

        List<Integer> successRecords = new ArrayList<Integer>();

        List<Object> results = new ArrayList<Object>();

        List<String> datasources = new ArrayList<String>();

        //calculate aggregate results
        Report finalReport = new Report();
        finalReport.setStatus(Status.FINISHED);
        for (Report report : easyBatchReports) {
            startTimes.add(report.getStartTime());
            endTimes.add(report.getEndTime());
            totalRecords += report.getTotalRecords();
            filteredRecords.addAll(report.getFilteredRecords());
            ignoredRecords.addAll(report.getIgnoredRecords());
            rejectedRecords.addAll(report.getRejectedRecords());
            errorRecords.addAll(report.getErrorRecords());
            successRecords.addAll(report.getSuccessRecords());
            if (report.getEasyBatchResult() != null) {
                results.add(report.getEasyBatchResult());
            }
            if (Status.ABORTED.equals(report.getStatus())) {
                finalReport.setStatus(Status.ABORTED);
            }
            datasources.add(report.getDataSource());
        }

        //merge results
        finalReport.setStartTime(Collections.min(startTimes));
        finalReport.setEndTime(Collections.max(endTimes));
        finalReport.setTotalRecords(totalRecords);
        for (Integer filteredRecord : filteredRecords) {
            finalReport.addFilteredRecord(filteredRecord);
        }
        for (Integer ignoredRecord : ignoredRecords) {
            finalReport.addIgnoredRecord(ignoredRecord);
        }
        for (Integer rejectedRecord : rejectedRecords) {
            finalReport.addRejectedRecord(rejectedRecord);
        }
        for (Integer errorRecord : errorRecords) {
            finalReport.addErrorRecord(errorRecord);
        }
        for (Integer successRecord : successRecords) {
            finalReport.addSuccessRecord(successRecord);
        }
        if (!results.isEmpty()) {
            finalReport.setEasyBatchResult(results);
        }

        //data sources
        StringBuilder stringBuilder = new StringBuilder();
        for (String dataSource : datasources) {
            stringBuilder.append(dataSource).append("\n");

        }
        finalReport.setDataSource(stringBuilder.toString());

        //return merged report
        return finalReport;
    }
}
