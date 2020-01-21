/*
 * The MIT License
 *
 *  Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.jeasy.batch.tutorials.advanced.parallel;

import org.jeasy.batch.core.filter.HeaderRecordFilter;
import org.jeasy.batch.core.job.Job;
import org.jeasy.batch.core.job.JobExecutor;
import org.jeasy.batch.core.reader.BlockingQueueRecordReader;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.extensions.integration.RoundRobinBlockingQueueRecordWriter;
import org.jeasy.batch.core.writer.StandardOutputRecordWriter;
import org.jeasy.batch.flatfile.DelimitedRecordMapper;
import org.jeasy.batch.flatfile.FlatFileRecordReader;
import org.jeasy.batch.tutorials.common.Tweet;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Arrays.asList;
import static org.jeasy.batch.core.job.JobBuilder.aNewJob;

public class RecordDispatching {

    private static final int THREAD_POOL_SIZE = 3;
    private static final int QUEUE_TIMEOUT = 1000;

    public static void main(String[] args) {

        // Input file tweets.csv
        Path tweets = Paths.get("src/main/resources/data/tweets.csv");

        // Create queues
        BlockingQueue<Record> workQueue1 = new LinkedBlockingQueue<>();
        BlockingQueue<Record> workQueue2 = new LinkedBlockingQueue<>();

        // Create a round robin record writer to distribute records to worker jobs
        RoundRobinBlockingQueueRecordWriter roundRobinBlockingQueueRecordWriter =
                                        new RoundRobinBlockingQueueRecordWriter(asList(workQueue1, workQueue2));

        // Build a master job to read records from the data source and dispatch them to worker jobs
        Job masterJob = aNewJob()
                .named("master-job")
                .reader(new FlatFileRecordReader(tweets))
                .filter(new HeaderRecordFilter())
                .mapper(new DelimitedRecordMapper<>(Tweet.class, "id", "user", "message"))
                .writer(roundRobinBlockingQueueRecordWriter)
                .build();

        // Build worker jobs
        Job workerJob1 = buildWorkerJob(workQueue1, "worker-job1");
        Job workerJob2 = buildWorkerJob(workQueue2, "worker-job2");

        // Create a job executor with 3 worker threads
        JobExecutor jobExecutor = new JobExecutor(THREAD_POOL_SIZE);

        // Submit jobs to executor
        jobExecutor.submitAll(masterJob, workerJob1, workerJob2);

        // Shutdown job executor
        jobExecutor.shutdown();

    }

    private static Job buildWorkerJob(BlockingQueue<Record> workQueue, String jobName) {
        return aNewJob()
                .named(jobName)
                .reader(new BlockingQueueRecordReader(workQueue, QUEUE_TIMEOUT))
                .writer(new StandardOutputRecordWriter())
                .build();
    }

}
