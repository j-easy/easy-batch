package org.easybatch.core.flow;

import org.easybatch.core.flow.predicate.JobExecutionPredicate;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class JobPipeline implements Callable<JobReport> {

    private Map<Job, JobExecutionPredicate> jobs = new HashMap<>();

    public JobPipeline(List<Job> jobs, List<JobExecutionPredicate> jobExecutionPredicates) {
        for (int i = 0; i < jobExecutionPredicates.size(); i++) {
            this.jobs.put(jobs.get(i), jobExecutionPredicates.get(i));
        }
        this.jobs.put(jobs.get(jobs.size() - 1), null); // the last job does not have a predicate
    }

    @Override
    public JobReport call() throws Exception {
        // TODO sanity checks
        JobReport jobReport = null;
        for (Job job : jobs.keySet()) {
            jobReport = job.call();
            JobExecutionPredicate jobExecutionPredicate = jobs.get(job);
            if (jobExecutionPredicate != null && !jobExecutionPredicate.apply(jobReport)) {
                    break;
            }
        }
        return jobReport;
    }
}
