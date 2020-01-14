/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.core.job;

import org.easybatch.core.util.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * Main class to execute {@link Job}s.
 *
 * <strong>Job executors must be explicitly shutdown using {@link JobExecutor#shutdown()}</strong>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JobExecutor {

    private ExecutorService executorService;

    /**
     * Create a job executor. The number of workers will be set to the number of available processors.
     */
    public JobExecutor() {
        executorService = newFixedThreadPool(getRuntime().availableProcessors());
    }

    /**
     * Create a job executor.
     *
     * @param nbWorkers number of worker threads
     */
    public JobExecutor(int nbWorkers) {
        executorService = newFixedThreadPool(nbWorkers);
    }

    /**
     * Create a job executor.
     *
     * @param executorService to use to execute jobs
     */
    public JobExecutor(ExecutorService executorService) {
        Utils.checkNotNull(executorService, "executor service");
        this.executorService = executorService;
    }

    /**
     * Execute a job synchronously.
     *
     * @param job to execute
     * @return the job report
     */
    public JobReport execute(Job job) {
        try {
            return executorService.submit(job).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Unable to execute job " + job.getName(), e);
        }
    }

    /**
     * Submit a job for asynchronous execution.
     *
     * @param job to execute
     * @return a future of the job report
     */
    public Future<JobReport> submit(Job job) {
        return executorService.submit(job);
    }

    /**
     * Submit jobs for execution.
     *
     * @param jobs to execute
     * @return the list of job reports in the same order of submission
     */
    public List<Future<JobReport>> submitAll(Job... jobs) {
        return submitAll(Arrays.asList(jobs));
    }

    /**
     * Submit jobs for execution.
     *
     * @param jobs to execute
     * @return the list of job reports in the same order of submission
     */
    public List<Future<JobReport>> submitAll(List<Job> jobs) {
        try {
            return executorService.invokeAll(jobs);
        } catch (InterruptedException e) {
            throw new RuntimeException("Unable to execute jobs", e);
        }
    }

    /**
     * Shutdown the job executor.
     */
    public void shutdown() {
        executorService.shutdown();
    }

    /**
     * Wait for jobs to terminate.
     */
    public void awaitTermination(long timeout, TimeUnit unit) {
        try {
            executorService.awaitTermination(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException("Job executor was interrupted while waiting");
        }
    }

}
