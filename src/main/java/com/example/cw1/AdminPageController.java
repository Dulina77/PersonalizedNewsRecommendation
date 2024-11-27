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
    private Button updateArticlesButton;
    @FXML
    private Button addArticleButton;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainNewsList.getItems().addAll(newsTitles);
        MainNewsList.setOnMouseClicked((this::articleSelectionMain));
    }

    public void articleSelectionMain(javafx.scene.input.MouseEvent event){
        if(event.getClickCount() == 2) {
            String clickedTitle = (String) MainNewsList.getSelectionModel().getSelectedItem();

            if (clickedTitle != null) {
                try {
                    String articleContent = DataBaseHandler.articleContentFetcher(clickedTitle);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cw1/FullArticle.fxml"));
                    Scene scene = new Scene(loader.load());

                    FullArticleController controller = loader.getController();
                    controller.setArticleDetails(clickedTitle, articleContent);
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

    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void AddArticle() throws IOException, SQLException {
        String title = newArticleTitle.getText();
        String content = newArticleContent.getText();

        KeywordExtraction keywordExtraction = new KeywordExtraction();
        String category = keywordExtraction.categorizeArticle(content);

        DataBaseHandler.insertArticle(title,content,category);
        SuccessMessage.setText("Article Added Successfully");


    }


}
