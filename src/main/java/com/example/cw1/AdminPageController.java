package com.example.cw1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminPageController extends HomeController{

    @FXML
    private Button DeleteArticlesButton;
    @FXML
    private Label SuccessMessage;
    @FXML
    private TextArea newArticleContent;
    @FXML
    private TextField newArticleTitle;
    @FXML
    private Pane contentPane;
    @FXML
    private Button BackButton;
    @FXML
    private Button addArticleButton;

    private Admin admin;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainNewsList.getItems().addAll(newsTitles);
        MainNewsList.setOnMouseClicked((this::articleSelection));    }


    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void AddArticle() throws SQLException, IOException {
        String title = newArticleTitle.getText();
        String content = newArticleContent.getText();

        if (title == null || title.isEmpty() || content == null || content.isEmpty()){
            SuccessMessage.setText("Please fill the both title and content fields to insert.");
            return;
        }
        admin.AddArticle(title,content);
        SuccessMessage.setText("Article Added Successfully");
    }

    void setAdmin(Admin admin){
        this.admin = admin;
    }

    public void articleSelection(javafx.scene.input.MouseEvent event){
        if(event.getClickCount() == 2) {
            String clickedTitle = (String) MainNewsList.getSelectionModel().getSelectedItem();

            if (clickedTitle != null) {
                try {
                    String articleContent = DataBaseHandler.articleContentFetcher(clickedTitle);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cw1/FullArticleAdmin.fxml"));
                    Scene scene = new Scene(loader.load());

                    FullArticleAdminController controller = loader.getController();
                    controller.setArticleDetails(clickedTitle, articleContent);
                    controller.setAdmin(admin);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
