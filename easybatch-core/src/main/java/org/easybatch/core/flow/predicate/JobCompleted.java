package org.easybatch.core.flow.predicate;

import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobStatus;

public class JobCompleted implements JobExecutionPredicate {

        public static JobCompleted jobCompleted() {
            return new JobCompleted();
        }

        @Override
        public boolean apply(JobReport jobReport) {
            return jobReport.getStatus() == JobStatus.COMPLETED;
        }
    }