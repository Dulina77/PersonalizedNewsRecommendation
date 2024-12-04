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
        loadHistoryTable();
    }

    public void loadHistoryTable() {
        if (user != null) {
            ArrayList<String> newsTitlesHistory = getNewsTitlesHistory(user);
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

    public void BackToSettings(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageAccount.fxml"));
        Scene scene = new Scene(loader.load());

        ManageAccountConroller controller = loader.getController();
        controller.setUser(user);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }



}
