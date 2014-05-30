package org.easybatch.tutorials.beanValidation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.*;

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

            String query = "CREATE TABLE if not exists product (\n" +
                    "  id varchar(4) NOT NULL PRIMARY KEY,\n" +
                    "  name varchar(32) NOT NULL,\n" +
                    "  description varchar(32) NOT NULL,\n" +
                    "  price decimal NOT NULL,\n" +
                    "  published boolean NOT NULL,\n" +
                    "  lastUpdate date NOT NULL,\n" +
                    ");";

            statement.executeUpdate(query);
            statement.close();
            connection.close();
    }

    public static void dumpProductTable() throws Exception {
        System.out.println("Loading products from the database...");
            Connection connection = DriverManager.getConnection(DATABASE_URL, "sa", "pwd");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from product");

            while (resultSet.next()) {
                System.out.println(
                        "Product : id= " + resultSet.getString("id") + " | " +
                                "name= " + resultSet.getString("name") + " | " +
                                "description= " + resultSet.getString("description") + " | " +
                                "price= " + resultSet.getDouble("price") + " | " +
                                "published= " + resultSet.getBoolean("published") + " | " +
                                "lastUpdate= " + resultSet.getDate("lastUpdate")
                );
            }

            resultSet.close();
            statement.close();
            connection.close();
    }
}