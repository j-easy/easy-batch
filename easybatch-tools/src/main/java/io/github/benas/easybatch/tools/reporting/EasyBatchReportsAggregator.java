package io.github.benas.easybatch.tools.reporting;

import io.github.benas.easybatch.core.api.EasyBatchReport;

/**
 * A service that merges multiple Easy Batch reports into a global report.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public interface EasyBatchReportsAggregator {

    /**
     * Merge multiple Easy Batch reports into a consolidated one.
     * @param easyBatchReports reports to merge
     * @return a merged report
     */
    EasyBatchReport aggregateReports(EasyBatchReport... easyBatchReports);

}
