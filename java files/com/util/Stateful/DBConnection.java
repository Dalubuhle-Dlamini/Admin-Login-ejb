package com.util.Stateful;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author dsihl
 */
public class DBConnection {

    private static String dbName = "ejbtest";
    private static String user = "root";
    private static String password = "";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/" + dbName;
    private static final String JDBC_USER = user;
    private static final String JDBC_PASSWORD = password;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Log or handle the exception as needed
            }
        }
    }
}
