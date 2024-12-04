package com.example.cw1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class FullArticleAdminController {
    @FXML
    private Label SuccessMessage;
    @FXML
    private Label TitleSpace;
    @FXML
    private Button DeleteArticle;
    @FXML
    private Button BackButton;
    @FXML
    private TextArea contentSpace;
    private Admin admin;
    private Article article;
    private int articleId;
    private String articleTitle;
    private String articleContent;


    public void setAdmin(Admin admin){
        this.admin = admin;
    }

    public void setArticleDetails(String title, String content) throws SQLException {
        articleTitle  = title;
        articleContent = content;
        contentSpace.setText(content);
        TitleSpace.setText(title);
        article = DataBaseHandler.getArticleDetails(title);
        articleId = article.getArticleId();
    }

    public void DeleteArticle(ActionEvent event) {
        ButtonType response = alertMessageDelete(articleTitle);

        if (response == ButtonType.OK) {
            try {
                boolean success = admin.DeleteArticle(articleTitle);
                if (success) {
                    SuccessMessage.setText("Article Deleted Successfully");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }



    public void BackToAdminPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminPage.fxml"));
        Parent root = loader.load();

        AdminPageController controller = loader.getController();
        controller.setAdmin(admin);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    public ButtonType alertMessageDelete(String articleTitle){
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);

        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to delete this article?");
        confirmationAlert.setContentText(articleTitle);

        return confirmationAlert.showAndWait().get();

    }
}
