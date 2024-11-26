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
import org.w3c.dom.events.MouseEvent;

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
    Article article;




    public void setArticleDetails(String title, String content) throws SQLException {
        articleTitle  = title;
        articleContent = content;
        contentSpace.setText(content);
        TitleSpace.setText(title);
        article = getArticleDetails(title);
        articleId = article.getArticleId();



    }

    public void BackToHomePage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent root = loader.load();

        HomeController controller = loader.getController();
        controller.setUser(user);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
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

    public void onLikeButtonClick(ActionEvent actionEvent) throws SQLException {
        recordAction(user.getUserName(),articleId, "Liked");
        recordScores(user.getUserName(),articleId, article.getCategory(), +4);
    }

    public void onDislIkeButtonClick(ActionEvent actionEvent) throws SQLException {
        recordAction(user.getUserName(),articleId, "Disliked");
        recordScores(user.getUserName(),articleId,article.getCategory(),-4);
    }

    public void onOpen(javafx.scene.input.MouseEvent event) throws SQLException {
        recordAction(user.getUserName(),articleId, "viewed");
        recordScores(user.getUserName(),articleId,article.getCategory(),+3);
    }


    public Article getArticleDetails(String title) throws SQLException {
        int articleId = 0;
        String category = null;
        String queryId = "SELECT articleId FROM article WHERE Title = ?";
        String queryCategory = "SELECT category FROM article WHERE Title = ?";

        try(Connection connection = DriverManager.getConnection(url,user_db,password)){
            PreparedStatement stmt1 = connection.prepareStatement(queryId);
            stmt1.setString(1, title);
            ResultSet resultSet1 = stmt1.executeQuery();

            PreparedStatement stmt2 = connection.prepareStatement(queryCategory);
            stmt2.setString(1, title);
            ResultSet resultSet2 = stmt2.executeQuery();

            if(resultSet1.next()){
                articleId = resultSet1.getInt("articleId");
            }
            if(resultSet2.next()){
                category = resultSet2.getString("category");
            }
            Article article = new Article(articleId, title ,articleContent,category);
            return article;
        }


    }


    public void recordScores(String username,int articleId,String category , int score) throws SQLException {
        String query = "INSERT into user_scores(user_name, articleid, category, score) VALUES (?,?,?,?) "+ "ON DUPLICATE KEY UPDATE score = score + ?";

        try (Connection connection = DriverManager.getConnection(url, user_db, password);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setInt(2, articleId);
            stmt.setString(3, category);
            stmt.setInt(4, score);
            stmt.setInt(5, score);
            stmt.executeUpdate();
            System.out.println(score);
        }
    }




}
