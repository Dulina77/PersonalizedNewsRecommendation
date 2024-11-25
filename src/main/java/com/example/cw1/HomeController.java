package com.example.cw1;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public Button ManageAccountButton;
    String url = "jdbc:mysql://localhost:3306/news";
    String user = "root";
    String password = "Dulina@123";

    public ListView newsList;

    ArrayList<String> news2 = articleFetcher();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newsList.getItems().addAll(news2);
    }

    public void BackToLogIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public ArrayList<String> articleFetcher(){
        ArrayList<String> news = new ArrayList<>();
        String query = "SELECT Title FROM article ";
        try(Connection connection = DriverManager.getConnection(url,user,password)) {
            PreparedStatement stmt = connection.prepareStatement((query));
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()){
                news.add(resultSet.getString("Title"));
            }


        return news;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
