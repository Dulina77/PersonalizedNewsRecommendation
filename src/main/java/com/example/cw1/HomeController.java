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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public Button ManageAccountButton;
    public ListView RecommendedNewsList;
    @FXML
    private Label welcomeMessage;


    private User user;

    public ListView MainNewsList;

    ArrayList<String> newsTitles = DataBaseHandler.articleTitleFetcher();
    List<String> recommendedTitles ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainNewsList.getItems().addAll(newsTitles);
        MainNewsList.setOnMouseClicked((this::articleSelectionMain));


        RecommendedNewsList.setOnMouseClicked((this::articleSelectionRecommendation));

    }

    public void BackToLogIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
                    controller.setUser(user);
                    controller.onOpen(event);


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

    public void articleSelectionRecommendation(javafx.scene.input.MouseEvent event){
        if(event.getClickCount() == 2) {
            String clickedTitle = (String) RecommendedNewsList.getSelectionModel().getSelectedItem();

            if (clickedTitle != null) {
                try {
                    String articleContent = DataBaseHandler.articleContentFetcher(clickedTitle);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cw1/FullArticle.fxml"));
                    Scene scene = new Scene(loader.load());

                    FullArticleController controller = loader.getController();
                    controller.setArticleDetails(clickedTitle, articleContent);
                    controller.setUser(user);
                    controller.onOpen(event);


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



    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            welcomeMessage.setText("Welcome" + " " + user.getFirstName());
            System.out.println(user.getUserName());

            recommendedTitles = recommendationTitles(user);
            RecommendedNewsList.getItems().clear();
            RecommendedNewsList.getItems().addAll(recommendedTitles);
            System.out.println(recommendedTitles);

        }
    }

    public List<String> recommendationTitles(User user){
        List<String> recommendedTitles = new ArrayList<>();
        ArticleRecommendation articleRecommendation =  new ArticleRecommendation();

        List<Article> recommendedArticles = articleRecommendation.getRecommendation(user.getUserName());

        for (int i = 0; i < recommendedArticles.size(); i++) {
            recommendedTitles.add(recommendedArticles.get(i).getTitle());
        }

        return recommendedTitles;
    }

    public void switchToHistoryPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cw1/History.fxml"));
        Scene scene = new Scene(loader.load());

        HistoryController controller = loader.getController();
        controller.setUser(user);
        controller.loadHistoryTable();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }


}
