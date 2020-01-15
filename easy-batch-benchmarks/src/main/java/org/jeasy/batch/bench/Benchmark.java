/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.jeasy.batch.bench;

import org.jeasy.batch.core.job.Job;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * JMH Class used to benchmark Easy Batch performance overhead.
 * Only record reading, parsing and mapping to domain objects are benchmarked.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@State(Scope.Benchmark)
public abstract class Benchmark {

    protected String customersFile;

    protected int customersCount;

    protected Job job;

    @Setup
    public void setup() throws Exception {
        customersCount = Integer.parseInt(System.getProperty("org.jeasy.batch.bench.count"));
    }

    @org.openjdk.jmh.annotations.Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runBatch() throws Exception {
        buildJob();
        job.call();
    }

    protected abstract void buildJob() throws Exception;

    @TearDown
    public void tearDown() {
        new File(customersFile).delete();
    }

}
