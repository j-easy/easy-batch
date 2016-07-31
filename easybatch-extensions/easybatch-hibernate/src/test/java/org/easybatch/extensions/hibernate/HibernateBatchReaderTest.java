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

package org.easybatch.extensions.hibernate;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.Batch;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;

public class HibernateBatchReaderTest {

    private static final int BATCH_SIZE = 2;

    private HibernateBatchReader<Tweet> hibernateBatchReader;

    @BeforeClass
    public static void init() throws Exception {
        DatabaseUtil.startEmbeddedDatabase();
        DatabaseUtil.populateTweetTable();
        DatabaseUtil.initializeSessionFactory();
    }

    @AfterClass
    public static void tearDown() {
        DatabaseUtil.closeSessionFactory();
        DatabaseUtil.cleanUpWorkingDirectory();
    }

    @Before
    public void setUp() {
        SessionFactory sessionFactory = DatabaseUtil.getSessionFactory();
        hibernateBatchReader = new HibernateBatchReader<>(BATCH_SIZE, sessionFactory, "from Tweet");
    }

    @Test
    public void testBatchProcessing() throws Exception {

        Job job = aNewJob()
                .reader(hibernateBatchReader)
                .processor(new RecordCollector())
                .build();

        JobReport jobReport = JobExecutor.execute(job);
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(2);

        List<Batch> batches = (List<Batch>) jobReport.getResult();
        assertThat(batches).isNotNull().hasSize(2);

        Batch batch1 = batches.get(0);
        assertThat(batch1.getPayload()).hasSize(2);
        Tweet tweet = (Tweet) batch1.getPayload().get(0).getPayload();
        assertThat(tweet).isNotNull();
        assertThat(tweet.getId()).isEqualTo(1);
        assertThat(tweet.getUser()).isEqualTo("foo");
        assertThat(tweet.getMessage()).isEqualTo("easy batch rocks! #EasyBatch");

        tweet = (Tweet) batch1.getPayload().get(1).getPayload();
        assertThat(tweet).isNotNull();
        assertThat(tweet.getId()).isEqualTo(2);
        assertThat(tweet.getUser()).isEqualTo("bar");
        assertThat(tweet.getMessage()).isEqualTo("@foo I do confirm :-)");

        Batch batch2 = batches.get(1);
        assertThat(batch2.getPayload()).hasSize(1);
        tweet = (Tweet) batch2.getPayload().get(0).getPayload();
        assertThat(tweet).isNotNull();
        assertThat(tweet.getId()).isEqualTo(3);
        assertThat(tweet.getUser()).isEqualTo("baz");
        assertThat(tweet.getMessage()).isEqualTo("yep");
    }
}
