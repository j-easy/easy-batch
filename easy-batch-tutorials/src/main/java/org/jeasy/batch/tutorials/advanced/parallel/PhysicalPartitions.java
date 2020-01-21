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

import org.jeasy.batch.core.job.DefaultJobReportMerger;
import org.jeasy.batch.core.job.Job;
import org.jeasy.batch.core.job.JobBuilder;
import org.jeasy.batch.core.job.JobExecutor;
import org.jeasy.batch.core.job.JobReport;
import org.jeasy.batch.core.job.JobReportMerger;
import org.jeasy.batch.core.writer.StandardOutputRecordWriter;
import org.jeasy.batch.flatfile.DelimitedRecordMapper;
import org.jeasy.batch.flatfile.FlatFileRecordReader;
import org.jeasy.batch.tutorials.common.Tweet;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Future;

public class PhysicalPartitions {

    private static final int THREAD_POOL_SIZE = 2;

    public static void main(String[] args) throws Exception {

        // Input file tweets-part1.csv
        Path tweetsPart1 = Paths.get(("src/main/resources/data/tweets-part1.csv"));

        // Input file tweets-part2.csv
        Path tweetsPart2 = Paths.get("src/main/resources/data/tweets-part2.csv");

        // Build worker jobs
        Job job1 = buildJob(tweetsPart1, "worker-job1");
        Job job2 = buildJob(tweetsPart2, "worker-job2");

        //create a job executor with 2 worker threads
        JobExecutor jobExecutor = new JobExecutor(THREAD_POOL_SIZE);

        List<Future<JobReport>> partialReports = jobExecutor.submitAll(job1, job2);

        //merge partial reports into a global one
        JobReport report1 = partialReports.get(0).get();
        JobReport report2 = partialReports.get(1).get();

        JobReportMerger reportMerger = new DefaultJobReportMerger();
        JobReport finalReport = reportMerger.mergerReports(report1, report2);
        System.out.println(finalReport);

        jobExecutor.shutdown();

    }

    private static Job buildJob(Path file, String jobName) {
        return JobBuilder.aNewJob()
                .named(jobName)
                .reader(new FlatFileRecordReader(file))
                .mapper(new DelimitedRecordMapper<>(Tweet.class, "id", "user", "message"))
                .writer(new StandardOutputRecordWriter())
                .build();
    }

}
