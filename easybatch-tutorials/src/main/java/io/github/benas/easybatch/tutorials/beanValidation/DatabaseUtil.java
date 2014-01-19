package io.github.benas.easybatch.tutorials.beanValidation;

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
    private static final String databaseURL = "jdbc:hsqldb:mem";

    public static void startEmbeddedDatabase() {
        try {
            Connection connection = DriverManager.getConnection(databaseURL, "sa", "pwd");
            Statement statement = connection.createStatement();

            String query = "CREATE TABLE if not exists product (\n" +
                    "  productId varchar(4) NOT NULL PRIMARY KEY,\n" +
                    "  name varchar(32) NOT NULL,\n" +
                    "  description varchar(32) NOT NULL,\n" +
                    "  price decimal NOT NULL,\n" +
                    "  published boolean NOT NULL,\n" +
                    "  lastUpdate date NOT NULL,\n" +
                    ");";

            statement.executeUpdate(query);
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dumpProductTable() {
        System.out.println("Loading products from the database...");
        try {
            Connection connection = DriverManager.getConnection(databaseURL, "sa", "pwd");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from product");

            while (resultSet.next()) {
                System.out.println(
                        "Product : productId= " + resultSet.getString("productId") + " | " +
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}