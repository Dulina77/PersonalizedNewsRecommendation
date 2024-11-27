package com.example.cw1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminPageController extends HomeController{

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
                    String articleContent = articleContentFetcher(clickedTitle);

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
        Parent root = FXMLLoader.load(getClass().getResource("AdminPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAddForm() {
        contentPane.getChildren().clear();

        // Create input fields dynamically
        TextField titleField = new TextField();
        titleField.setPromptText("Enter Title");

        TextField contentField = new TextField();
        contentField.setPromptText("Enter Content");

        Button saveButton = new Button("Save Article");
        saveButton.setOnAction(event -> {
            try {
                saveArticle(titleField.getText(), contentField.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Add fields to contentPane
        contentPane.getChildren().addAll(titleField, contentField, saveButton);
    }

    private void saveArticle(String title, String content) throws IOException {
        KeywordExtraction keywordExtraction = new KeywordExtraction();
        String category = keywordExtraction.categorizeArticle(content);

        String query = "INSERT INTO article (title, content, category) VALUES (?, ?, ?)\" + \"ON DUPLICATE KEY UPDATE content = ?, category = ?";

    }
}
