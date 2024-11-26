package com.example.cw1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;


public class FullArticleController {

    @FXML
    private Button BackButton;
    @FXML
    private Button LikeButton;
    @FXML
    private Button DislikeButton;
    @FXML
    private TextArea contentSpace;
    @FXML
    private Label TitleSpace;
    public static final String url = "jdbc:mysql://localhost:3306/news";
    public static final String user_db = "root";
    public static final String password = "Dulina@123";

    private User user = null;
    private String articleTitle = null;
    private String articleContent = null;
    private int articleId;




    public void setArticleDetails(String title, String content) throws SQLException {
        articleTitle  = title;
        articleContent = content;
        contentSpace.setText(content);
        TitleSpace.setText(title);
        articleId = getArticleId(title);
    }

    public void BackToHomePage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void recordAction(String username, int articleId, String action){
        String query = "INSERT INTO history(user_name, article_Id, action) values (?,?,?)";
        try
            (Connection connection = DriverManager.getConnection(url, user_db,password);
            PreparedStatement stmt = connection.prepareStatement(query);){
            stmt.setString(1,username);
            stmt.setInt(2,articleId);
            stmt.setString(3,action);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void setUser(User user){
        this.user = user;
    }

    public void onLikeButtonClick(ActionEvent actionEvent){
        recordAction(user.getUserName(),articleId, "Liked");
    }

    public void onDislIkeButtonClick(ActionEvent actionEvent){
        recordAction(user.getUserName(),articleId, "Disliked");

    }

    public int getArticleId(String title) throws SQLException {
        int articleId = 0;
        String query = "SELECT articleId FROM article WHERE Title = ?";
        try(Connection connection = DriverManager.getConnection(url,user_db,password)){
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, title);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()){
                articleId = resultSet.getInt("articleId");
            }
        }
        return articleId;
    }


}
