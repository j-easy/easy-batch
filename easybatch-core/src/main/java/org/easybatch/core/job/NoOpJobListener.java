package org.easybatch.core.job;

import org.easybatch.core.listener.JobListener;

class NoOpJobListener implements JobListener {
    @Override
    public void beforeJobStart(JobParameters jobParameters) {

    }

    @Override
    public void afterJobEnd(JobReport jobReport) {

    }
}
