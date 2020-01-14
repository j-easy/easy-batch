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
import org.easybatch.core.reader.IterableRecordReader;
import org.easybatch.test.common.AbstractDatabaseTest;
import org.easybatch.test.common.Tweet;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;

public class HibernateRecordWriterTest extends AbstractDatabaseTest {

    private JobExecutor jobExecutor;
    private HibernateRecordWriter hibernateRecordWriter;

    @Before
    public void setUp() throws Exception {
        addScript("data.sql");
        super.setUp();
        jobExecutor = new JobExecutor();
        Configuration configuration = new Configuration();
        configuration.configure("/org/easybatch/extensions/hibernate/hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        hibernateRecordWriter = new HibernateRecordWriter(sessionFactory);
    }

    @Test
    public void testRecordWriting() throws Exception {
        int nbTweetsToInsert = 5;
        List<Tweet> tweets = createTweets(nbTweetsToInsert);

        Job job = aNewJob()
                .batchSize(2)
                .reader(new IterableRecordReader(tweets))
                .writer(hibernateRecordWriter)
                .build();

        JobReport jobReport = jobExecutor.execute(job);

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(nbTweetsToInsert);
        assertThat(jobReport.getMetrics().getWriteCount()).isEqualTo(nbTweetsToInsert);

        int nbTweetsInDatabase = countRowsIn("tweet");
        assertThat(nbTweetsInDatabase).isEqualTo(nbTweetsToInsert);
    }

    @Test
    public void testRecordWritingWithError() throws Exception {
        int nbTweetsToInsert = 5;
        int batchSize = 3; // two batches: [1,2,3] and [4,5]
        List<Tweet> tweets = createTweets(nbTweetsToInsert);
        // The following will make the second batch to fail
        tweets.get(4).setUser("ThisIsAVeryLongUsernameThatWillCauseAnError");
        Job job = aNewJob()
                .batchSize(batchSize)
                .reader(new IterableRecordReader(tweets))
                .writer(hibernateRecordWriter)
                .build();

        JobReport jobReport = jobExecutor.execute(job);

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getReadCount()).isEqualTo(nbTweetsToInsert);
        assertThat(jobReport.getMetrics().getWriteCount()).isEqualTo(3L);

        int nbTweetsInDatabase = countRowsIn("tweet");
        assertThat(nbTweetsInDatabase).isEqualTo(3);
    }

    private List<Tweet> createTweets(int nbTweetsToInsert) {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 1; i <= nbTweetsToInsert; i++) {
            tweets.add(new Tweet(i, "user " + i, "hello " + i));
        }
        return tweets;
    }


    @After
    public void tearDown() throws Exception {
        jobExecutor.shutdown();
        super.tearDown();
    }

}
