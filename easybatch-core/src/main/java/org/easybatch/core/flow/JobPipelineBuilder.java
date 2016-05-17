package org.easybatch.core.flow;

import org.easybatch.core.flow.predicate.JobExecutionPredicate;
import org.easybatch.core.job.Job;

import java.util.ArrayList;
import java.util.List;

public class JobPipelineBuilder {

    private List<Job> jobs = new ArrayList<>();
    private List<JobExecutionPredicate> jobExecutionPredicates = new ArrayList<>();

    public JobPipelineBuilder() {}

    public static JobPipelineBuilder aNewJobPipelineBuilder() {
        return new JobPipelineBuilder();
    }

    // TODO ensure methods are called in the following order: startWith, when, then, when , then, .., build

    public JobPipelineBuilder startWith(Job job) {
        jobs.add(job);
        return this;
    }

    public JobPipelineBuilder when(JobExecutionPredicate jobExecutionPredicate) {
        jobExecutionPredicates.add(jobExecutionPredicate);
        return this;
    }

    public JobPipelineBuilder then(Job job) {
        jobs.add(job);
        return this;
    }

    public JobPipeline build() {
        return new JobPipeline(jobs, jobExecutionPredicates);
    }
}
