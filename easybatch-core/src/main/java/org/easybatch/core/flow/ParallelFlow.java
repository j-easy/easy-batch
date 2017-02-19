package org.easybatch.core.flow;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class ParallelFlow extends Flow {

    private List<Job> jobs;
    private JobExecutor jobExecutor;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public JobReport call() {
        List<Future<JobReport>> futures = jobExecutor.submitAll(jobs);
        int finishedJobs = jobs.size();
        // FIXME polling futures for completion, not sure this is the best way to run callables in parallel
        // and wait them for completion (use CompletionService??)
        while (finishedJobs > 0) {
            for (Future<JobReport> future : futures) {
                if (future.isDone()) {
                    try {
                        JobReport jobReport = future.get();
                    } catch (Exception e) {
                       // TODO handle exception
                    }
                    finishedJobs--;
                }
            }
        }
        return null;
    }
}
