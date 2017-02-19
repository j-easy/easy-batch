package org.easybatch.core.flow;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.JobStatus;

import java.util.List;

public class SequentialFlow extends Flow {

    private List<Job> jobs;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public JobReport call() {
        JobReport jobReport = null;
        for (Job job : jobs) {
            jobReport = job.call(); // stop on first failed job
            if (jobReport.getStatus().equals(JobStatus.FAILED)) {
                break;
            }
        }
        return jobReport;
    }
}
