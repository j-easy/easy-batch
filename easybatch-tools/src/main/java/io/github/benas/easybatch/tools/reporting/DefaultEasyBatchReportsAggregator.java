package io.github.benas.easybatch.tools.reporting;

import io.github.benas.easybatch.core.api.EasyBatchReport;

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
 * </ul>
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class DefaultEasyBatchReportsAggregator implements EasyBatchReportsAggregator {

    /**
     * Merge multiple Easy Batch reports into a consolidated one.
     * @param easyBatchReports reports to merge
     * @return a merged report
     */
    public EasyBatchReport aggregateReports(EasyBatchReport... easyBatchReports) {

        List<Long> startTimes = new ArrayList<Long>();

        List<Long> endTimes = new ArrayList<Long>();

        long totalRecords = 0;

        List<Long> filteredRecords = new ArrayList<Long>();

        List<Long> ignoredRecords = new ArrayList<Long>();

        List<Long> rejectedRecords = new ArrayList<Long>();

        List<Long> errorRecords = new ArrayList<Long>();

        List<Long> successRecords = new ArrayList<Long>();

        Map<Long, Long> processingTimes = new HashMap<Long, Long>();

        List<Object> results = new ArrayList<Object>();

        List<String> datasources = new ArrayList<String>();

        //calculate aggregate results
        EasyBatchReport easyBatchFinalReport = new EasyBatchReport();
        for (EasyBatchReport easyBatchReport : easyBatchReports) {
            startTimes.add(easyBatchReport.getStartTime());
            endTimes.add(easyBatchReport.getEndTime());
            totalRecords += easyBatchReport.getTotalRecords();
            filteredRecords.addAll(easyBatchReport.getFilteredRecords());
            ignoredRecords.addAll(easyBatchReport.getIgnoredRecords());
            rejectedRecords.addAll(easyBatchReport.getRejectedRecords());
            errorRecords.addAll(easyBatchReport.getErrorRecords());
            successRecords.addAll(easyBatchReport.getSuccessRecords());
            processingTimes.putAll(easyBatchReport.getProcessingTimes());
            results.add(easyBatchReport.getEasyBatchResult());
            datasources.add(easyBatchReport.getDataSource());
        }

        //merge results
        easyBatchFinalReport.setStartTime(Collections.min(startTimes));
        easyBatchFinalReport.setEndTime(Collections.max(endTimes));
        easyBatchFinalReport.setTotalRecords(totalRecords);
        for (Long filteredRecord : filteredRecords) {
            easyBatchFinalReport.addFilteredRecord(filteredRecord);
        }
        for (Long ignoredRecord : ignoredRecords) {
            easyBatchFinalReport.addIgnoredRecord(ignoredRecord);
        }
        for (Long rejectedRecord : rejectedRecords) {
            easyBatchFinalReport.addRejectedRecord(rejectedRecord);
        }
        for (Long errorRecord : errorRecords) {
            easyBatchFinalReport.addErrorRecord(errorRecord);
        }
        for (Long successRecord : successRecords) {
            easyBatchFinalReport.addSuccessRecord(successRecord);
        }
        for (Map.Entry<Long, Long> entry : processingTimes.entrySet()) {
            easyBatchFinalReport.addProcessingTime(entry.getKey(), entry.getValue());
        }
        easyBatchFinalReport.setEasyBatchResult(results);

        //data sources
        StringBuilder stringBuilder = new StringBuilder();
        for (String dataSource : datasources) {
            stringBuilder.append(dataSource).append("\n");

        }
        easyBatchFinalReport.setDataSource(stringBuilder.toString());

        //return merged report
        return easyBatchFinalReport;
    }
}
