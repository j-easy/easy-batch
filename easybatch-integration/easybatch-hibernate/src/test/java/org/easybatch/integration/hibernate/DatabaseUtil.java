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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.File;
import java.sql.*;

/**
 * Utility class for embedded database and hibernate services.
 */
public class DatabaseUtil {

    private static final String DATABASE_URL = "jdbc:hsqldb:mem";
    public static final String USER = "sa";
    public static final String PASSWORD = "pwd";

    /*
     * Hibernate related utilities
     */
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void initializeSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure("/org/easybatch/integration/hibernate/hibernate.cfg.xml");

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public static Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public static void closeSessionFactory() {
        sessionFactory.close();
    }

    /*
     * HSQL utility methods
     */

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
    }

    public static void startEmbeddedDatabase() throws Exception {
        //do not let hsqldb reconfigure java.util.logging used by easy batch
        System.setProperty("hsqldb.reconfig_logging", "false");
        Connection connection = getConnection();
        createTweetTable(connection);
        populateTweetTable(connection);
        connection.close();
    }

    public static void createTweetTable(Connection connection) throws Exception{
        Statement statement = connection.createStatement();

        String query = "CREATE TABLE if not exists tweet (\n" +
                "  id integer NOT NULL PRIMARY KEY,\n" +
                "  user varchar(32) NOT NULL,\n" +
                "  message varchar(140) NOT NULL,\n" +
                ");";

        statement.executeUpdate(query);
        statement.close();
    }

    public static void populateTweetTable(Connection connection) throws Exception {
        executeQuery(connection,
                "INSERT INTO tweet VALUES (1,'foo','easy batch rocks! #EasyBatch');");
        executeQuery(connection,
                "INSERT INTO tweet VALUES (2,'bar','@foo I do confirm :-)');");

    }

    public static void executeQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        int i = statement.executeUpdate(query);
        if (i == -1) {
            System.err.println("database error : " + query);
        }
        statement.close();
    }

    public static void dumpTweetTable() throws Exception {
        System.out.println("Loading tweets from the database...");
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from tweet");
        while (resultSet.next()) {
            System.out.println(
                    "Tweet : id= " + resultSet.getString("id") + " | " +
                            "user= " + resultSet.getString("user") + " | " +
                            "message= " + resultSet.getString("message")
            );
        }

        resultSet.close();
        statement.close();
        connection.close();
    }

    public static void cleanUpWorkingDirectory() {
        //delete hsqldb tmp files
        new File("mem.tmp").delete();
        new File("mem.log").delete();
        new File("mem.properties").delete();
        new File("mem.script").delete();
        new File("mem.lck").delete();
    }
}