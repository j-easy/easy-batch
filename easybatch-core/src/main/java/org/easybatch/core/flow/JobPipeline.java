package org.easybatch.core.flow;

import org.easybatch.core.flow.predicate.JobExecutionPredicate;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobReport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class JobPipeline implements Callable<JobReport> {

    private final List<Job> jobs = new ArrayList<>();

    private final List<JobExecutionPredicate> jobExecutionPredicates = new ArrayList<>();

    public JobPipeline(List<Job> jobs, List<JobExecutionPredicate> jobExecutionPredicates) {
        this.jobs.addAll(jobs);
        this.jobExecutionPredicates.addAll(jobExecutionPredicates);
    }

    @Override
    public JobReport call() throws Exception {
        // TODO sanity checks
        JobReport jobReport = null;
        int i = 0;
        for (Job job : jobs) {
            jobReport = job.call();
            if (i < jobExecutionPredicates.size()) {
                JobExecutionPredicate jobExecutionPredicate = jobExecutionPredicates.get(i);
                i++;
                if (!jobExecutionPredicate.apply(jobReport)) {
                    break;
                }
            }
        }
        return jobReport;
    }
}
