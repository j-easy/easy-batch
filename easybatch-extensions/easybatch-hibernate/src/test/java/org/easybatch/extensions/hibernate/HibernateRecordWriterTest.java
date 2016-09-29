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
import org.easybatch.core.reader.IterableRecordReader;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

public class HibernateRecordWriterTest {

    private JobExecutor jobExecutor;
    private JdbcTemplate jdbcTemplate;
    private EmbeddedDatabase embeddedDatabase;
    private HibernateRecordWriter hibernateRecordWriter;

    @Before
    public void setUp() throws Exception {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(HSQL)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
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

        int nbTweetsInDatabase = countTweetsInDatabase();
        assertThat(nbTweetsInDatabase).isEqualTo(nbTweetsToInsert);
    }

    private List<Tweet> createTweets(int nbTweetsToInsert) {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 1; i <= nbTweetsToInsert; i++) {
            tweets.add(new Tweet(i, "user " + i, "hello " + i));
        }
        return tweets;
    }

    private int countTweetsInDatabase() throws SQLException {
        return jdbcTemplate.queryForObject("select count(*) from tweet", Integer.class);
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
