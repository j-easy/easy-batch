package org.easybatch.core.flow.predicate;

import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobStatus;

public class JobAborted implements JobExecutionPredicate {

        public static JobAborted jobAborted() {
            return new JobAborted();
        }

        @Override
        public boolean apply(JobReport jobReport) {
            return jobReport.getStatus() == JobStatus.ABORTED;
        }
    }