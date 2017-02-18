package org.easybatch.core.job.pipeline.predicate;

import org.easybatch.core.job.JobReport;

import java.util.List;

public interface StagePredicate {
    boolean apply(List<JobReport> jobReports);
}
