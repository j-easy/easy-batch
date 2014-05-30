package org.easybatch.tutorials.advanced;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Utility class for embedded database and hibernate services.
 */
public class DatabaseUtil {

    private static final String DATABASE_URL = "jdbc:hsqldb:mem";

    /*
     * Hibernate related utilities
     */
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void initializeSessionFactory() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
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

    public static void startEmbeddedDatabase() throws Exception {
            Connection connection = DriverManager.getConnection(DATABASE_URL, "sa", "pwd");
            Statement statement = connection.createStatement();

            String query = "CREATE TABLE IF NOT EXISTS greeting (\n" +
                    "  id int IDENTITY NOT NULL PRIMARY KEY,\n" +
                    "  name varchar(32) DEFAULT NULL,\n" +
                    ");";

            statement.executeUpdate(query);
            statement.close();
            connection.close();
    }

    public static Connection getDatabaseConnection() throws Exception {
        return DriverManager.getConnection(DATABASE_URL, "sa", "pwd");
    }

}