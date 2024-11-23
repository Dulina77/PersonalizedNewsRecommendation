package com.example.cw1;
import java.sql.*;

public class DataBase {
    public static final String url = "jdbc:mysql://localhost:3306/news";
    public static final String user = "root";
    public static final String password = "Dulina@123";



    public static void insertUser(User user) {
        String query = "INSERT INTO user_final (user_name,first_name, last_name, email,password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3,user.getLastName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5,user.getPassword());
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}
