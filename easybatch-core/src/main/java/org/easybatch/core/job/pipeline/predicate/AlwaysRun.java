package org.easybatch.core.job.pipeline.predicate;

import org.easybatch.core.job.JobReport;

import java.util.List;

public class AlwaysRun implements StagePredicate {
    @Override
    public boolean apply(List<JobReport> jobReports) {
        return true;
    }
}
