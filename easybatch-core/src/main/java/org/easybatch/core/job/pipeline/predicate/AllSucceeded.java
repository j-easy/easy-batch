package org.easybatch.core.job.pipeline.predicate;

import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobStatus;

import java.util.List;

public class AllSucceeded implements StagePredicate {
    @Override
    public boolean apply(List<JobReport> jobReports) {
        for (JobReport jobReport : jobReports) {
            if (jobReport.getStatus().equals(JobStatus.FAILED)) {
                return false;
            }
        }
        return true;
    }
}
