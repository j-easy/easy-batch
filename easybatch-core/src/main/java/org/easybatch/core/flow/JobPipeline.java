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
