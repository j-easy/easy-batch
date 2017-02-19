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

import org.easybatch.core.flow.predicate.JobPredicate;
import org.easybatch.core.job.Job;

import static org.easybatch.core.flow.predicate.JobCompleted.completed;

public class JobFlowBuilder {

    private String name;
    private Job toExecute, nextOnPredicateSuccess, nextOnPredicateFailure;
    private JobPredicate predicate;

    public JobFlowBuilder() {}

    public static JobFlowBuilder aNewJobFlow() {
        return new JobFlowBuilder();
    }

    public JobFlowBuilder named(String name) {
        this.name = name;
        return this;
    }

    public JobFlowBuilder execute(Job job) {
        this.toExecute = job;
        return this;
    }

    public JobFlowBuilder when(JobPredicate predicate) {
        this.predicate = predicate;
        return this;
    }

    public JobFlowBuilder then(Job nextOnPredicateSuccess) {
        this.nextOnPredicateSuccess = nextOnPredicateSuccess;
        return this;
    }

    public JobFlowBuilder otherwise(Job nextOnPredicateFailure) {
        this.nextOnPredicateFailure = nextOnPredicateFailure;
        return this;
    }

    public JobFlow build() {
        // assert at least toExecute, predicate and nextOnSuccess are provided
        return new JobFlow(name, toExecute, predicate, nextOnPredicateSuccess, nextOnPredicateFailure);
    }


    public static void main(String[] args) {
        Job j1= null, j2 = null, j3= null, j4= null, j5= null, j6= null, j7= null;

        JobFlow jobFlow1 = JobFlowBuilder.aNewJobFlow()
                .named("f1")
                .execute(j2)
                .when(completed())
                .then(j4)
                .otherwise(j5)
                .build();

        JobFlow jobFlow2 = JobFlowBuilder.aNewJobFlow()
                .named("f1")
                .execute(j3)
                .when(completed())
                .then(j6)
                .otherwise(j7)
                .build();

        JobFlow jobFlow3 = JobFlowBuilder.aNewJobFlow()
                .named("f1")
                .execute(j1)
                .when(completed())
                .then(jobFlow1)
                .otherwise(jobFlow2)
                .build();
    }
}
