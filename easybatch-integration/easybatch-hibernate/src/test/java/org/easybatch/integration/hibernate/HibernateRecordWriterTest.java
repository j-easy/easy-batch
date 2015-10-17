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

import org.easybatch.core.api.JobReport;
import org.easybatch.core.mapper.GenericRecordMapper;
import org.easybatch.core.reader.IterableRecordReader;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.impl.JobBuilder.aNewJob;

public class HibernateRecordWriterTest {

    private Session session;

    private HibernateRecordWriter<Tweet> hibernateRecordWriter;

    @BeforeClass
    public static void initDatabase() throws Exception {
        DatabaseUtil.startEmbeddedDatabase();
        DatabaseUtil.initializeSessionFactory();
    }

    @Before
    public void setUp() throws Exception {
        session = DatabaseUtil.getSessionFactory().openSession();
        hibernateRecordWriter = new HibernateRecordWriter<Tweet>(session);
    }

    @Test
    public void testSingleRecordWriting() throws Exception {

        Integer nbTweetsToInsert = 5;

        List<Tweet> tweets = createTweets(nbTweetsToInsert);

        JobReport jobReport = aNewJob()
                .reader(new IterableRecordReader<Tweet>(tweets))
                .mapper(new GenericRecordMapper())
                .writer(hibernateRecordWriter)
                .pipelineEventListener(new HibernateTransactionPipelineListener(session))
                .jobEventListener(new HibernateSessionJobListener(session))
                .call();

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(valueOf(nbTweetsToInsert));
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(valueOf(nbTweetsToInsert));

        int nbTweetsInDatabase = countTweetsInDatabase();

        assertThat(nbTweetsInDatabase).isEqualTo(nbTweetsToInsert);
    }

    private List<Tweet> createTweets(Integer nbTweetsToInsert) {
        List<Tweet> tweets = new ArrayList<Tweet>();
        for (int i = 1; i <= nbTweetsToInsert; i++) {
            tweets.add(new Tweet(i, "user " + i, "hello " + i));
        }
        return tweets;
    }

    private int countTweetsInDatabase() throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from tweet");
        int nbTweets = 0;
        while (resultSet.next()) {
            nbTweets++;
        }
        resultSet.close();
        statement.close();
        connection.close();
        return nbTweets;
    }

    @AfterClass
    public static void shutdownDatabase() throws Exception {
        DatabaseUtil.closeSessionFactory();
        DatabaseUtil.cleanUpWorkingDirectory();
    }

}
