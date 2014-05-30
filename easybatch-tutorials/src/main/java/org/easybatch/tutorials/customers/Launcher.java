/*
 * The MIT License
 *
 *   Copyright (c) 2014, Mahmoud Ben Hassine (md.benhassine@gmail.com)
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

package org.easybatch.tutorials.customers;

import org.easybatch.core.api.EasyBatchReport;
import org.easybatch.core.impl.EasyBatchEngine;
import org.easybatch.core.impl.EasyBatchEngineBuilder;
import org.easybatch.jdbc.JdbcRecordReader;
import org.easybatch.tutorials.customers.etl.CustomerMapper;
import org.easybatch.tutorials.customers.etl.CustomerProcessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
* Main class to run the customers tutorial.
 *
* @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
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
                .registerRecordReader(new JdbcRecordReader(connection, "select * from customer"))
                .registerRecordMapper(new CustomerMapper())
                .registerRecordProcessor(new CustomerProcessor())
                .build();

        // Run easy batch engine
        EasyBatchReport easyBatchReport = easyBatchEngine.call();
        System.out.println(easyBatchReport);

    }

    private static void populateEmbeddedDB(Connection connection) throws Exception {

        executeQuery(connection, "CREATE TABLE customer (\n" +
                "  id int IDENTITY NOT NULL PRIMARY KEY,\n" +
                "  firstName varchar(32) DEFAULT NULL,\n" +
                "  lastName varchar(32) DEFAULT NULL,\n" +
                "  birthDate date DEFAULT NULL,\n" +
                "  phone varchar(32) DEFAULT NULL,\n" +
                "  email varchar(32) DEFAULT NULL,\n" +
                "  street varchar(32) DEFAULT NULL,\n" +
                "  zipCode varchar(32) DEFAULT NULL,\n" +
                "  city varchar(32) DEFAULT NULL,\n" +
                "  country varchar(32) DEFAULT NULL,\n" +
                ");");

        executeQuery(connection,
                "INSERT INTO customer VALUES (1,'foo','bar','1990-12-13','0123456789','foo@bar.org','street1','zipCode1','london','uk');");
        executeQuery(connection,
                "INSERT INTO customer VALUES (2,'bar','foo','2000-12-13','9876543210','bar@foo.org','street2','zipCode2','new york','usa');");

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