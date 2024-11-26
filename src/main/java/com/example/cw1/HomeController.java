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
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    public Button ManageAccountButton;
    @FXML
    private Label welcomeMessage;
    String url = "jdbc:mysql://localhost:3306/news";
    String user_db = "root";
    String password = "Dulina@123";

    private User user;

    public ListView newsList;

    ArrayList<String> newsTitles = articleTitleFetcher();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newsList.getItems().addAll(newsTitles);

        newsList.setOnMouseClicked((this::articleSelection));

    }

    public void BackToLogIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public ArrayList<String> articleTitleFetcher(){
        ArrayList<String> title = new ArrayList<>();
        String query = "SELECT Title FROM article ";
        try(Connection connection = DriverManager.getConnection(url,user_db,password)) {
            PreparedStatement stmt = connection.prepareStatement((query));
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()){
                title.add(resultSet.getString("Title"));
            }


        return title;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void articleSelection(javafx.scene.input.MouseEvent event){
        if(event.getClickCount() == 2) {
            String clickedTitle = (String) newsList.getSelectionModel().getSelectedItem();

            if (clickedTitle != null) {
                try {
                    String articleContent = articleContentFetcher(clickedTitle);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cw1/FullArticle.fxml"));
                    Scene scene = new Scene(loader.load());

                    FullArticleController controller = loader.getController();
                    controller.setArticleDetails(clickedTitle, articleContent);
                    controller.setUser(user);


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

    public String articleContentFetcher(String title){
        ArrayList<String> content = new ArrayList<>();
        String query = "SELECT content FROM article WHERE Title = ?";
        try(Connection connection = DriverManager.getConnection(url,user_db,password)) {
            PreparedStatement stmt = connection.prepareStatement((query));
            stmt.setString(1,title);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()){
                return resultSet.getString("content");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Article content not accessible";
    }

    public void setUser(User user){
        this.user = user;
        if (user != null){
            welcomeMessage.setText("Welcome"+ " "+ user.getFirstName());
            System.out.println(user.getUserName());

        }

    }

}
