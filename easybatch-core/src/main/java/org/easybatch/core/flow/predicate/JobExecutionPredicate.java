package org.easybatch.core.flow.predicate;

import org.easybatch.core.job.JobReport;

public interface JobExecutionPredicate {
      boolean apply(final JobReport jobReport);
}