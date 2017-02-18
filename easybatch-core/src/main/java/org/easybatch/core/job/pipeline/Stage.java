package org.easybatch.core.job.pipeline;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.pipeline.predicate.AlwaysRun;
import org.easybatch.core.job.pipeline.predicate.StagePredicate;

import java.util.Arrays;
import java.util.List;

public class Stage {

    private List<Job> jobs;
    private StagePredicate predicate;

    public Stage(List<Job> jobs) {
        this.jobs = jobs;
        this.predicate = new AlwaysRun();
    }

    public Stage(Job... jobs) {
        this(Arrays.asList(jobs));
    }

    public Stage(StagePredicate predicate, List<Job> jobs) {
        this.jobs = jobs;
        this.predicate = predicate;
    }

    public Stage(StagePredicate predicate, Job... jobs) {
        this(predicate, Arrays.asList(jobs));
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public StagePredicate getPredicate() {
        return predicate;
    }
}
