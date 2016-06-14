package org.easybatch.core.flow;

import org.easybatch.core.flow.predicate.JobExecutionPredicate;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class JobPipeline implements Callable<Map<Job, JobReport>> {

    private final List<Job> jobs = new ArrayList<>();

    private final List<JobExecutionPredicate> jobExecutionPredicates = new ArrayList<>();

    Map<Job, JobReport> reports = new HashMap<>();

    public JobPipeline(List<Job> jobs, List<JobExecutionPredicate> jobExecutionPredicates) {
        this.jobs.addAll(jobs);
        this.jobExecutionPredicates.addAll(jobExecutionPredicates);
    }

    @Override
    public Map<Job, JobReport> call() throws Exception {
        // TODO sanity checks
        JobReport jobReport;
        int i = 0;
        for (Job job : jobs) {
            jobReport = job.call();
            reports.put(job, jobReport);
            if (i < jobExecutionPredicates.size()) {
                JobExecutionPredicate jobExecutionPredicate = jobExecutionPredicates.get(i);
                i++;
                if (!jobExecutionPredicate.apply(jobReport)) {
                    break;
                }
            }
        }
        return reports;
    }
}
