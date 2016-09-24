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

import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.*;

import java.io.File;
import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcRecordReaderTest {

    static JDBCDataSource dataSource;
    private static Connection connection;
    private static String query;
    private JdbcRecordReader jdbcRecordReader;

    @BeforeClass
    public static void initDatabase() throws Exception {
        dataSource = new JDBCDataSource();
        dataSource.setUser("sa");
        dataSource.setPassword("pwd");
        dataSource.setUrl("jdbc:hsqldb:mem");
        connection = dataSource.getConnection();
        createTweetTable(connection);
        populateTweetTable(connection);
        query = "select * from tweet";
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
    }

    private static void executeQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

    @Before
    public void setUp() throws Exception {
        jdbcRecordReader = new JdbcRecordReader(dataSource, query);
        jdbcRecordReader.open();
    }

    @Test
    public void testReadRecord() throws Exception {
        JdbcRecord actual = jdbcRecordReader.readRecord();

        assertThat(actual).isNotNull();
        assertThat(actual.getHeader().getNumber()).isEqualTo(1);

        ResultSet payload = actual.getPayload();
        assertThat(payload.getInt(1)).isEqualTo(1);
        assertThat(payload.getString(2)).isEqualTo("foo");
        assertThat(payload.getString(3)).isEqualTo("easy batch rocks! #EasyBatch");
    }

    @Test
    public void testMaxRowsParameter() throws Exception {
        jdbcRecordReader = new JdbcRecordReader(dataSource, query);
        jdbcRecordReader.setMaxRows(1);
        jdbcRecordReader.open();
        assertThat(jdbcRecordReader.readRecord()).isNotNull();
        assertThat(jdbcRecordReader.readRecord()).isNull();
    }

    @After
    public void tearDown() throws Exception {
        /*
         * The connection will be closed in @AfterClass method.
         * If done here, subsequent tests do not find the connection
         */
        //jdbcRecordReader.close();
    }

}
