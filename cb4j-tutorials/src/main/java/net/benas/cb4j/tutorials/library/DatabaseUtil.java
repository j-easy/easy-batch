package net.benas.cb4j.tutorials.library;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.*;

/**
 * Utility class for embedded database and hibernate services.
 */
public class DatabaseUtil {

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
    private static final String databaseURL = "jdbc:hsqldb:mem:booklibrary";

    public static void startEmbeddedDatabase() {
        try {
            Connection connection = DriverManager.getConnection(databaseURL, "sa", "pwd");
            Statement statement = connection.createStatement();

            String query = "CREATE TABLE book (\n" +
                    "  isbn varchar(13) NOT NULL PRIMARY KEY,\n" +
                    "  title varchar(32) NOT NULL,\n" +
                    "  author varchar(32) NOT NULL,\n" +
                    "  publicationDate date NOT NULL,\n" +
                    ");";

            statement.executeUpdate(query);
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dumpBookTable() {
        System.out.println("Loading books from database...");
        try {
            Connection connection = DriverManager.getConnection(databaseURL, "sa", "pwd");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from book");

            while (resultSet.next()) {
                System.out.println(
                        "Book : isbn= " + resultSet.getString("isbn") + " | " +
                                "title= " + resultSet.getString("title") + " | " +
                                "author= " + resultSet.getString("author") + " | " +
                                "publicationDate= " + resultSet.getString("publicationDate")
                );
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}