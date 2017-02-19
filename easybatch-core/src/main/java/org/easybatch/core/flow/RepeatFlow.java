package org.easybatch.core.flow;

import org.easybatch.core.flow.predicate.JobPredicate;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobReport;

public class RepeatFlow extends Flow {

    private Job job;
    private JobPredicate predicate;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public JobReport call() {
        JobReport jobReport;
        do {
            jobReport = job.call();
        } while (predicate.apply(jobReport));
        return jobReport;
    }
}
