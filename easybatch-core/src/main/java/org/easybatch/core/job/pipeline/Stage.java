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
