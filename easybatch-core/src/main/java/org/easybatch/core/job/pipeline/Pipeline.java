package org.easybatch.core.job.pipeline;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.job.pipeline.predicate.StagePredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class Pipeline implements Callable<PipelineReport> {

    private List<Stage> stages;

    private JobExecutor jobExecutor;

    public Pipeline(List<Stage> stages) {
        this.stages = stages;
        jobExecutor = new JobExecutor();
    }

    @Override
    public PipelineReport call() throws Exception {
        PipelineReport pipelineReport = new PipelineReport();
        List<JobReport> previousJobsReports = new ArrayList<>();
        for (Stage stage : stages) {
            StagePredicate predicate = stage.getPredicate();
            if (!predicate.apply(previousJobsReports)) {
                break;
            }
            StageReport stageReport = new StageReport();
            Job[] jobs = (Job[]) stage.getJobs().toArray(); // to list when submitAll accepts a list
            List<Future<JobReport>> futures = jobExecutor.submitAll(jobs);
            previousJobsReports.clear();
            int finishedJobs = jobs.length;
            // FIXME polling futures for completion, not sure this is the best way to run callables in parallel and wait them for completion (use CompletionService??)
            while (finishedJobs > 0) {
                for (Future<JobReport> future : futures) {
                    if (future.isDone()) {
                        JobReport jobReport = future.get();
                        stageReport.add(jobReport);
                        previousJobsReports.add(jobReport);
                        finishedJobs--;
                    }
                }
            }
            pipelineReport.add(stageReport);
        }
        return pipelineReport;
    }

}
