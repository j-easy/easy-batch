/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.jdbc;

import org.easybatch.core.job.*;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.Record;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.record.PayloadExtractor.extractPayloads;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

public class JdbcIntegrationTest {

    private EmbeddedDatabase embeddedDatabase;
    private String query;
    private JobExecutor jobExecutor;

    @Before
    public void setUp() throws Exception {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(HSQL)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        query = "select * from tweet";
        jobExecutor = new JobExecutor();
    }

    @Test
    public void testDatabaseProcessing() throws Exception {

        RecordCollector<Tweet> recordCollector = new RecordCollector<>();
        Job job = JobBuilder.aNewJob()
                .reader(new JdbcRecordReader(embeddedDatabase, query))
                .mapper(new JdbcRecordMapper<>(Tweet.class, "id", "user", "message"))
                .processor(recordCollector)
                .build();

        JobReport jobReport = jobExecutor.execute(job);

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(2);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getWriteCount()).isEqualTo(2);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.COMPLETED);

        List<Record<Tweet>> records = recordCollector.getRecords();
        List<Tweet> tweets = extractPayloads(records);

        assertThat(tweets).hasSize(2);

        Tweet tweet = tweets.get(0);
        assertThat(tweet.getId()).isEqualTo(1);
        assertThat(tweet.getUser()).isEqualTo("foo");
        assertThat(tweet.getMessage()).isEqualTo("easy batch rocks! #EasyBatch");

        tweet = tweets.get(1);
        assertThat(tweet.getId()).isEqualTo(2);
        assertThat(tweet.getUser()).isEqualTo("bar");
        assertThat(tweet.getMessage()).isEqualTo("@foo I do confirm :-)");
    }

    @After
    public void tearDown() throws Exception {
        jobExecutor.shutdown();
        embeddedDatabase.shutdown();
    }

    @AfterClass
    public static void cleanup() throws Exception {
        //delete hsqldb tmp files
        new File("mem.log").delete();
        new File("mem.properties").delete();
        new File("mem.script").delete();
    }

}
