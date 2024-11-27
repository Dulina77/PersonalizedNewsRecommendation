package com.example.cw1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class HistoryController {

    @FXML
    private Button BackButton;
    String url = "jdbc:mysql://localhost:3306/news";
    String user_db = "root";
    String password = "Dulina@123";

    @FXML
    private ListView HistoryTable;
    private User user;


    public void setUser(User user) {
        this.user = user;
        // Once the user is set, populate the history table
        loadHistoryTable();
    }

    public void loadHistoryTable() {
        if (user != null) {
            // Get the news titles history from the database
            ArrayList<String> newsTitlesHistory = getNewsTitlesHistory(user);
            // Set the items in the ListView
            HistoryTable.getItems().setAll(newsTitlesHistory);
        }
    }

    public ArrayList<String> getNewsTitlesHistory(User user) {
        ArrayList<String> newsTitlesHistory = new ArrayList<String>();

        String query = "SELECT Title FROM article WHERE articleId IN (SELECT article_Id FROM history WHERE user_name = ?)";
        try (Connection connection = DriverManager.getConnection(url, user_db, password)) {
            PreparedStatement stmt = connection.prepareStatement((query));
            stmt.setString(1, user.getUserName());
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                newsTitlesHistory.add(resultSet.getString("Title"));
            }
            return newsTitlesHistory;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void BackToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
