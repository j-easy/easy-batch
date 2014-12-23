package org.easybatch.tools.reporting;

import org.easybatch.core.api.Report;

/**
 * A service that merges multiple reports into a global report.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public interface ReportsAggregator {

    /**
     * Merge multiple reports into a consolidated one.
     * @param reports reports to merge
     * @return a merged report
     */
    Report aggregateReports(Report... reports);

}
