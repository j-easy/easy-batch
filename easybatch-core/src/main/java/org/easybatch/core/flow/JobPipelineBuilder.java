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
