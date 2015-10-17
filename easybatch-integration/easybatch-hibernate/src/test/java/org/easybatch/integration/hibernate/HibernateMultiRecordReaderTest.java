/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.integration.hibernate;

import org.easybatch.core.api.Job;
import org.easybatch.core.api.Report;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.MultiRecord;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.impl.JobBuilder.aNewJob;

public class HibernateMultiRecordReaderTest {

    private static final int CHUNK_SIZE = 2;
    
    private HibernateMultiRecordReader<Tweet> hibernateMultiRecordReader;

    @BeforeClass
    public static void init() throws Exception {
        DatabaseUtil.startEmbeddedDatabase();
        DatabaseUtil.populateTweetTable();
        DatabaseUtil.initializeSessionFactory();
    }

    @Before
    public void setUp() {
        SessionFactory sessionFactory = DatabaseUtil.getSessionFactory();
        hibernateMultiRecordReader = new HibernateMultiRecordReader<Tweet>(CHUNK_SIZE, sessionFactory, "from Tweet");
    }

    @Test
    public void testChunkProcessing() throws Exception {
        
        Job job = aNewJob()
                .reader(hibernateMultiRecordReader)
                .processor(new RecordCollector<Tweet>())
                .build();

        Report report = job.call();
        assertThat(report.getTotalRecords()).isEqualTo(2);

        List<MultiRecord> multiRecords = (List<MultiRecord>) report.getJobResult();
        assertThat(multiRecords).isNotNull().hasSize(2);

        MultiRecord chunk1 = multiRecords.get(0);
        assertThat(chunk1.getPayload().size()).isEqualTo(2);
        Tweet tweet = (Tweet) chunk1.getPayload().get(0).getPayload();
        assertThat(tweet).isNotNull();
        assertThat(tweet.getId()).isEqualTo(1);
        assertThat(tweet.getUser()).isEqualTo("foo");
        assertThat(tweet.getMessage()).isEqualTo("easy batch rocks! #EasyBatch");

        tweet = (Tweet) chunk1.getPayload().get(1).getPayload();
        assertThat(tweet).isNotNull();
        assertThat(tweet.getId()).isEqualTo(2);
        assertThat(tweet.getUser()).isEqualTo("bar");
        assertThat(tweet.getMessage()).isEqualTo("@foo I do confirm :-)");
        
        MultiRecord chunk2 = multiRecords.get(1);
        assertThat(chunk2.getPayload().size()).isEqualTo(1);
        tweet = (Tweet) chunk2.getPayload().get(0).getPayload();
        assertThat(tweet).isNotNull();
        assertThat(tweet.getId()).isEqualTo(3);
        assertThat(tweet.getUser()).isEqualTo("baz");
        assertThat(tweet.getMessage()).isEqualTo("yep");
    }

    @AfterClass
    public static void tearDown() {
        DatabaseUtil.closeSessionFactory();
        DatabaseUtil.cleanUpWorkingDirectory();

    }
}
