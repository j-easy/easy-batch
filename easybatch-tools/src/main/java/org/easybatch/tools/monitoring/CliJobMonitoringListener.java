package org.easybatch.tools.monitoring;

import org.easybatch.core.jmx.JobMonitoringListener;
import org.easybatch.core.job.JobReport;

import static java.lang.String.format;

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
