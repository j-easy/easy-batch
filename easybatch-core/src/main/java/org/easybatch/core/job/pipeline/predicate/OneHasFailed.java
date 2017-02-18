package org.easybatch.core.job.pipeline.predicate;

import org.easybatch.core.job.JobReport;

import java.util.List;

public class OneHasFailed implements StagePredicate {

    @Override
    public boolean apply(List<JobReport> jobReports) {
        return !new AllSucceeded().apply(jobReports);
    }
}
