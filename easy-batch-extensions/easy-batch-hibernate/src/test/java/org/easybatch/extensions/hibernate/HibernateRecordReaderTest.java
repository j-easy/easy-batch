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
package org.easybatch.extensions.hibernate;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.test.common.AbstractDatabaseTest;
import org.easybatch.test.common.Tweet;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;
import static org.easybatch.core.record.PayloadExtractor.extractPayloads;

public class HibernateRecordReaderTest extends AbstractDatabaseTest {

    private HibernateRecordReader<Tweet> hibernateRecordReader;

    @Before
    public void setUp() throws Exception {
        addScript("data.sql");
        super.setUp();
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

        List<Tweet> tweets = extractPayloads(recordCollector.getRecords());
        assertThat(tweets).hasSize(2);

        Tweet tweet = tweets.get(0);
        assertThat(tweet).isEqualTo(new Tweet(1, "foo", "easy batch rocks! #EasyBatch"));
        tweet = tweets.get(1);
        assertThat(tweet).isEqualTo(new Tweet(2, "bar", "@foo I do confirm :-)"));
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

}
