package com.example.cw1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/news";
        String user = "root";
        String password = "Dulina@123";

        try {
            // Try directly without manually loading the driver
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful.");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}