package com.example.cw1;
import java.sql.*;

public class DataBase {
    private static final String url = "jdbc:mysql://localhost:3306/news";
    private static final String user = "root";
    private static final String password = "Dulina@123";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Failed to establish a connection.");
            }
        }
        return connection;
    }












}
