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
        article = DataBaseHandler.getArticleDetails(title);
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


    public void setUser(User user){
        this.user = user;
    }

    public void onLikeButtonClick(ActionEvent actionEvent) throws SQLException {
        DataBaseHandler.recordAction(user.getUserName(),articleId, "Liked");
        DataBaseHandler.recordScores(user.getUserName(),articleId, article.getCategory(), +4);
        user.getProfile().addToLiked(article);
    }

    public void onDislIkeButtonClick(ActionEvent actionEvent) throws SQLException {
        DataBaseHandler.recordAction(user.getUserName(),articleId, "Disliked");
        DataBaseHandler.recordScores(user.getUserName(),articleId,article.getCategory(),-4);
        user.getProfile().addToDisliked(article);
    }

    public void onOpen(javafx.scene.input.MouseEvent event) throws SQLException {
        DataBaseHandler.recordAction(user.getUserName(),articleId, "viewed");
        DataBaseHandler.recordScores(user.getUserName(),articleId,article.getCategory(),+3);
        user.getProfile().addToHistory(article);
    }





}
