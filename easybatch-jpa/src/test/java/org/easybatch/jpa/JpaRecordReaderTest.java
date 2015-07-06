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

package org.easybatch.jpa;

import org.easybatch.core.record.GenericRecord;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link JpaRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JpaRecordReaderTest {

    private static final String DATABASE_URL = "jdbc:hsqldb:mem";

    private static Connection connection;

    private static EntityManagerFactory entityManagerFactory;

    private static String query;

    private JpaRecordReader<Tweet> jpaRecordReader;

    @BeforeClass
    public static void initDatabase() throws Exception {
        connection = DriverManager.getConnection(DATABASE_URL, "sa", "pwd");
        createTweetTable(connection);
        populateTweetTable(connection);
        entityManagerFactory = Persistence.createEntityManagerFactory("tweet");
        query = "from Tweet";
    }

    @Before
    public void setUp() throws Exception {
        jpaRecordReader = new JpaRecordReader<Tweet>(entityManagerFactory, query, Tweet.class);
        jpaRecordReader.open();
    }

    @Test
    public void testHasNextRecord() throws Exception {
        assertThat(jpaRecordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void testTotalRecords() throws Exception {
        assertThat(jpaRecordReader.getTotalRecords()).isNotNull().isEqualTo(2);
    }

    @Test
    public void testReadNextRecord() throws Exception {
        GenericRecord<Tweet> record = jpaRecordReader.readNextRecord();
        long recordNumber = record.getHeader().getNumber();
        Tweet tweet = record.getPayload();

        assertThat(recordNumber).isEqualTo(1);
        assertThat(tweet).isNotNull();
        assertThat(tweet.getId()).isEqualTo(1);
        assertThat(tweet.getUser()).isEqualTo("foo");
        assertThat(tweet.getMessage()).isEqualTo("easy batch rocks! #EasyBatch");
    }

    @Test
    public void testMaxResultsParameter() throws Exception {
        jpaRecordReader.close();
        jpaRecordReader = new JpaRecordReader<Tweet>(entityManagerFactory, query, Tweet.class);
        jpaRecordReader.setMaxResults(1);
        jpaRecordReader.open();

        jpaRecordReader.readNextRecord();
        assertThat(jpaRecordReader.hasNextRecord()).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void maxResultParameterMustBeAtLeastEqualToOne() throws Exception {
        jpaRecordReader.setMaxResults(0);
    }

    @AfterClass
    public static void shutdownDatabase() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        //delete hsqldb tmp files
        new File("mem.log").delete();
        new File("mem.properties").delete();
        new File("mem.script").delete();
        new File("mem.tmp").delete();
    }

    private static void createTweetTable(Connection connection) throws Exception {
        Statement statement = connection.createStatement();
        String query = "CREATE TABLE if not exists tweet (\n" +
                "  id integer NOT NULL PRIMARY KEY,\n" +
                "  user varchar(32) NOT NULL,\n" +
                "  message varchar(140) NOT NULL,\n" +
                ");";
        statement.executeUpdate(query);
        statement.close();
    }

    private static void populateTweetTable(Connection connection) throws Exception {
        executeQuery(connection, "INSERT INTO tweet VALUES (1,'foo','easy batch rocks! #EasyBatch');");
        executeQuery(connection, "INSERT INTO tweet VALUES (2,'bar','@foo I do confirm :-)');");
    }

    private static void executeQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

}
