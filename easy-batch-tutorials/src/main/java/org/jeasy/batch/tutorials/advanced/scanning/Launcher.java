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

package org.jeasy.batch.tutorials.advanced.scanning;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sql.DataSource;

import org.jeasy.batch.core.job.Job;
import org.jeasy.batch.core.job.JobExecutor;
import org.jeasy.batch.core.job.JobReport;
import org.jeasy.batch.flatfile.DelimitedRecordMapper;
import org.jeasy.batch.flatfile.FlatFileRecordReader;
import org.jeasy.batch.jdbc.BeanPropertiesPreparedStatementProvider;
import org.jeasy.batch.jdbc.JdbcRecordWriter;
import org.jeasy.batch.tutorials.common.DatabaseUtil;
import org.jeasy.batch.tutorials.common.Tweet;

import static org.jeasy.batch.core.job.JobBuilder.aNewJob;

/**
 * Main class to run the batch scanning tutorial.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class Launcher {

    public static void main(String[] args) throws Exception {
        // Start embedded database server
        DatabaseUtil.startEmbeddedDatabase();
        DatabaseUtil.deleteAllTweets();

        DataSource dataSource = DatabaseUtil.getDataSource();
        String query = "INSERT INTO tweet VALUES (?,?,?);";
        String[] fields = {"id", "user", "message"};

        // Build a batch job
        Path tweets = Paths.get(args.length != 0 ? args[0] : "easy-batch-tutorials/src/main/resources/data/tweets-with-invalid-records.csv");
        Path skippedTweets = Paths.get(args.length != 0 ? args[1] : "easy-batch-tutorials/target/skipped-tweets.csv");
        Job job = aNewJob()
                .batchSize(5)
                .enableBatchScanning(true)
                .reader(new FlatFileRecordReader(tweets))
                .mapper(new DelimitedRecordMapper<>(Tweet.class, fields))
                .writer(new JdbcRecordWriter(dataSource, query, new BeanPropertiesPreparedStatementProvider(Tweet.class, fields)))
                .writerListener(new ScannedRecordListener(skippedTweets))
                .build();
        
        // Execute the job
        JobExecutor jobExecutor = new JobExecutor();
        JobReport jobReport = jobExecutor.execute(job);
        jobExecutor.shutdown();

        System.out.println(jobReport);

        // Dump tweet table to check inserted data
        DatabaseUtil.dumpTweetTable();

        // Shutdown embedded database server and delete temporary files
        DatabaseUtil.cleanUpWorkingDirectory();

    }

}
