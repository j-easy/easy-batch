package org.easybatch.core.flow.predicate;

import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobStatus;

public class JobFailed implements JobExecutionPredicate {

        public static JobFailed jobFailed() {
            return new JobFailed();
        }

        @Override
        public boolean apply(JobReport jobReport) {
            return jobReport.getStatus() == JobStatus.FAILED;
        }
    }