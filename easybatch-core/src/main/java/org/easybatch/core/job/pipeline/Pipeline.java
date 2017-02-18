/**
 * The MIT License
 *
 *   Copyright (c) 2017, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
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
