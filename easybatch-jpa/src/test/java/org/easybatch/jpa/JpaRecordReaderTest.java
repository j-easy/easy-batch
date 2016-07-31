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

package org.easybatch.jpa;

import org.easybatch.core.record.GenericRecord;
import org.junit.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

public class JpaRecordReaderTest {

    private static final String DATABASE_URL = "jdbc:hsqldb:mem";

    private static final int FETCH_SIZE = 2;

    private static Connection connection;

    private static EntityManagerFactory entityManagerFactory;

    private JpaRecordReader<Tweet> jpaRecordReader;

    @BeforeClass
    public static void initDatabase() throws Exception {
        connection = DriverManager.getConnection(DATABASE_URL, "sa", "pwd");
        createTweetTable(connection);
        populateTweetTable(connection);
        entityManagerFactory = Persistence.createEntityManagerFactory("tweet");
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
        String query = "DROP TABLE IF EXISTS tweet";
        statement.executeUpdate(query);
        query = "CREATE TABLE tweet (\n" +
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
        executeQuery(connection, "INSERT INTO tweet VALUES (3,'baz','yep');");
        executeQuery(connection, "INSERT INTO tweet VALUES (4,'toto','what?');");
    }

    private static void executeQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

    @Before
    public void setUp() throws Exception {
        String query = "from Tweet";
        jpaRecordReader = new JpaRecordReader<>(entityManagerFactory, query, Tweet.class);
        jpaRecordReader.setFetchSize(FETCH_SIZE);
        jpaRecordReader.open();
    }

    @Test
    public void testHasNextRecord() throws Exception {
        assertThat(jpaRecordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void testTotalRecords() throws Exception {
        assertThat(jpaRecordReader.getTotalRecords()).isNull();
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
    public void testPaging() {
        int nbRecords = 0;
        while (jpaRecordReader.hasNextRecord()) {
            jpaRecordReader.readNextRecord();
            nbRecords++;
        }
        assertThat(nbRecords).isEqualTo(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fetchSizeParameterMustBeAtLeastEqualToOne() throws Exception {
        jpaRecordReader.setFetchSize(0);
    }

    @After
    public void tearDown() {
        jpaRecordReader.close();
    }

}
