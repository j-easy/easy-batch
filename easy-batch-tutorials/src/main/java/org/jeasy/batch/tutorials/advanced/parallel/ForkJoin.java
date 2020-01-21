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

import org.jeasy.batch.core.job.Job;
import org.jeasy.batch.core.job.JobExecutor;
import org.jeasy.batch.core.processor.RecordProcessor;
import org.jeasy.batch.core.reader.BlockingQueueRecordReader;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.writer.BlockingQueueRecordWriter;
import org.jeasy.batch.extensions.integration.RoundRobinBlockingQueueRecordWriter;
import org.jeasy.batch.core.writer.StandardOutputRecordWriter;
import org.jeasy.batch.jdbc.JdbcRecordMapper;
import org.jeasy.batch.jdbc.JdbcRecordReader;
import org.jeasy.batch.tutorials.common.DatabaseUtil;
import org.jeasy.batch.tutorials.common.Tweet;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Arrays.asList;
import static org.jeasy.batch.core.job.JobBuilder.aNewJob;

public class ForkJoin {

    private static final int THREAD_POOL_SIZE = 4;
    private static final int QUEUE_TIMEOUT = 1000;

    public static void main(String[] args) throws Exception {

        // Start embedded database server
        DatabaseUtil.startEmbeddedDatabase();

        // Get a data source
        DataSource dataSource = DatabaseUtil.getDataSource();

        // Create queues
        BlockingQueue<Record> workQueue1 = new LinkedBlockingQueue<>();
        BlockingQueue<Record> workQueue2 = new LinkedBlockingQueue<>();
        BlockingQueue<Record> joinQueue = new LinkedBlockingQueue<>();

        // Build jobs
        Job forkJob = buildForkJob("fork-job", dataSource, asList(workQueue1, workQueue2));
        Job workerJob1 = buildWorkerJob("worker-job1", workQueue1, joinQueue);
        Job workerJob2 = buildWorkerJob("worker-job2", workQueue2, joinQueue);
        Job joinJob = buildJoinJob("join-job", joinQueue);

        // Create a job executor to call jobs in parallel
        JobExecutor jobExecutor = new JobExecutor(THREAD_POOL_SIZE);

        // Submit jobs to run in parallel
        jobExecutor.submitAll(forkJob, workerJob1, workerJob2, joinJob);

        // Shutdown job executor
        jobExecutor.shutdown();

        // Shutdown embedded database server and delete temporary files
        DatabaseUtil.cleanUpWorkingDirectory();
    }

    private static Job buildForkJob(String jobName, DataSource dataSource, List<BlockingQueue<Record>> workQueues) {
        return aNewJob()
                .named(jobName)
                .reader(new JdbcRecordReader(dataSource, "select * from tweet"))
                .mapper(new JdbcRecordMapper<>(Tweet.class, "id", "user", "message"))
                .writer(new RoundRobinBlockingQueueRecordWriter(workQueues))
                .build();
    }

    private static Job buildWorkerJob(String jobName, BlockingQueue<Record> workQueue, BlockingQueue<Record> joinQueue) {
        return aNewJob()
                .named(jobName)
                .reader(new BlockingQueueRecordReader(workQueue, QUEUE_TIMEOUT))
                .processor(new TweetProcessor(jobName))
                .writer(new BlockingQueueRecordWriter(joinQueue))
                .build();
    }

    private static Job buildJoinJob(String jobName, BlockingQueue<Record> joinQueue) {
        return aNewJob()
                .named(jobName)
                .reader(new BlockingQueueRecordReader(joinQueue, QUEUE_TIMEOUT))
                .writer(new StandardOutputRecordWriter())
                .build();
    }

    private static class TweetProcessor implements RecordProcessor<Record, Record> {

        private String workerName;

        TweetProcessor(String workerName) {
            this.workerName = workerName;
        }

        @Override
        public Record processRecord(Record record) {
            System.out.println(workerName + ": processing tweet " + record.getPayload());
            return record;
        }

    }

}
