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

import org.easybatch.core.job.*;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.GenericRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.record.PayloadExtractor.extractPayloads;

@SuppressWarnings("unchecked")
public class JdbcIntegrationTest {

    private static final String DATABASE_URL = "jdbc:hsqldb:mem";

    private static final String DATA_SOURCE_NAME = "Connection URL: jdbc:hsqldb:mem | Query string: select id, name from person";

    private Connection connection;

    private String query;

    @BeforeClass
    public static void init() {
        System.setProperty("hsqldb.reconfig_logging", "false");
    }

    @Before
    public void setUp() throws Exception {
        connection = DriverManager.getConnection(DATABASE_URL, "sa", "pwd");
        createPersonTable(connection);
        populatePersonTable(connection);
        query = "select id, name from person";
    }

    @Test
    public void testDatabaseProcessing() throws Exception {

        Job job = JobBuilder.aNewJob()
                .reader(new JdbcRecordReader(connection, query))
                .mapper(new JdbcRecordMapper(Person.class, "id", "name"))
                .processor(new RecordCollector())
                .build();

        JobReport jobReport = JobExecutor.execute(job);

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(2);
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(2);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(jobReport.getParameters().getDataSource()).isEqualTo(DATA_SOURCE_NAME);

        List<GenericRecord<Person>> records = (List<GenericRecord<Person>>) jobReport.getResult();
        List<Person> persons = extractPayloads(records);

        assertThat(persons).hasSize(2);

        final Person person1 = persons.get(0);
        assertThat(person1.getId()).isEqualTo(1);
        assertThat(person1.getName()).isEqualTo("foo");

        final Person person2 = persons.get(1);
        assertThat(person2.getId()).isEqualTo(2);
        assertThat(person2.getName()).isEqualTo("bar");
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        //delete hsqldb tmp files
        new File("mem.log").delete();
        new File("mem.properties").delete();
        new File("mem.script").delete();
        new File("mem.tmp").delete();
    }

    private void createPersonTable(Connection connection) throws Exception {
        Statement statement = connection.createStatement();
        String query = "DROP TABLE IF EXISTS person";
        statement.executeUpdate(query);
        query = "CREATE TABLE person (\n" +
                "  id integer NOT NULL PRIMARY KEY,\n" +
                "  name varchar(32) NOT NULL,\n" +
                ");";
        statement.executeUpdate(query);
        statement.close();
    }

    private void populatePersonTable(Connection connection) throws Exception {
        executeQuery(connection, "INSERT INTO person VALUES (1,'foo');");
        executeQuery(connection, "INSERT INTO person VALUES (2,'bar');");
    }

    private void executeQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

}
