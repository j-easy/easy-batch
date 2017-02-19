package org.easybatch.core.flow;

import org.easybatch.core.flow.predicate.JobPredicate;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobReport;

public class ConditionalFlow extends Flow {

    private String name;
    private Job toExecute, nextOnPredicateSuccess, nextOnPredicateFailure;
    private JobPredicate predicate;

    public ConditionalFlow(String name, Job toExecute, JobPredicate predicate, Job nextOnPredicateSuccess, Job nextOnPredicateFailure) {
        this.name = name;
        this.toExecute = toExecute;
        this.nextOnPredicateSuccess = nextOnPredicateSuccess;
        this.nextOnPredicateFailure = nextOnPredicateFailure;
        this.predicate = predicate;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JobReport call() {
        JobReport jobReport = toExecute.call();
        if (predicate.apply(jobReport)) {
            jobReport = nextOnPredicateSuccess.call();
        } else {
            if (nextOnPredicateFailure != null) { // else is optional
                jobReport = nextOnPredicateFailure.call();
            }
        }
        return jobReport;
    }
}
