/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
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

package io.github.benas.easybatch.tutorials.helloworld.jdbc;

import io.github.benas.easybatch.core.api.EasyBatchReport;
import io.github.benas.easybatch.core.impl.EasyBatchEngine;
import io.github.benas.easybatch.core.impl.EasyBatchEngineBuilder;
import io.github.benas.easybatch.jdbc.JdbcRecordMapper;
import io.github.benas.easybatch.jdbc.JdbcRecordReader;
import io.github.benas.easybatch.tutorials.helloworld.csv.Greeting;
import io.github.benas.easybatch.tutorials.helloworld.csv.GreetingProcessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
* Main class to run the hello world jdbc tutorial.
 *
* @author benas (md.benhassine@gmail.com)
*/
public class Launcher {

    public static void main(String[] args) throws Exception {

        //do not let hsqldb reconfigure java.util.logging used by easy batch
        System.setProperty("hsqldb.reconfig_logging", "false");

        // create an embedded hsqldb in-memory database
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem", "sa", "");
        populateEmbeddedDB(connection);

        // Build an easy batch engine
        EasyBatchEngine easyBatchEngine = new EasyBatchEngineBuilder()
                .registerRecordReader(new JdbcRecordReader(connection, "select * from greeting"))
                .registerRecordMapper(new JdbcRecordMapper<Greeting>(Greeting.class, new String[]{"sequence", "name"}))
                .registerRecordProcessor(new GreetingProcessor())
                .build();

        // Run easy batch engine
        EasyBatchReport easyBatchReport = easyBatchEngine.call();

        // Print the batch execution report
        System.out.println("easyBatchReport = " + easyBatchReport);

    }

    private static void populateEmbeddedDB(Connection connection) throws Exception {

        executeQuery(connection, "CREATE TABLE greeting (\n" +
                "  sequence int IDENTITY NOT NULL PRIMARY KEY,\n" +
                "  name varchar(32) DEFAULT NULL,\n" +
                ");");

        executeQuery(connection, "INSERT INTO greeting VALUES (1,'foo');");
        executeQuery(connection, "INSERT INTO greeting VALUES (2,'bar');");

    }

    private static void executeQuery(Connection connection, String query) throws SQLException {

        Statement statement;
        statement = connection.createStatement();
        int i = statement.executeUpdate(query);
        if (i == -1) {
            System.err.println("database error : " + query);
        }
        statement.close();
    }

}