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
import org.easybatch.core.record.Record;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

public class HibernateRecordReaderTest {

    private EmbeddedDatabase embeddedDatabase;
    private HibernateRecordReader<Tweet> hibernateRecordReader;

    @Before
    public void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(HSQL)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        Configuration configuration = new Configuration();
        configuration.configure("/org/easybatch/extensions/hibernate/hibernate.cfg.xml");

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        hibernateRecordReader = new HibernateRecordReader<>(sessionFactory, "from Tweet");
    }

    @Test
    public void testRecordReading() throws Exception {

        RecordCollector<Tweet> recordCollector = new RecordCollector<>();
        Job job = aNewJob()
                .reader(hibernateRecordReader)
                .processor(recordCollector)
                .build();

        JobReport jobReport = new JobExecutor().execute(job);
        assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(2);

        List<Record<Tweet>> tweets = recordCollector.getRecords();

        assertThat(tweets).hasSize(2);

        Tweet tweet = tweets.get(0).getPayload();
        assertThat(tweet).isNotNull();
        assertThat(tweet.getId()).isEqualTo(1);
        assertThat(tweet.getUser()).isEqualTo("foo");
        assertThat(tweet.getMessage()).isEqualTo("easy batch rocks! #EasyBatch");

        tweet = tweets.get(1).getPayload();
        assertThat(tweet).isNotNull();
        assertThat(tweet.getId()).isEqualTo(2);
        assertThat(tweet.getUser()).isEqualTo("bar");
        assertThat(tweet.getMessage()).isEqualTo("@foo I do confirm :-)");
    }

    @After
    public void tearDown() throws Exception {
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
