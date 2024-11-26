package com.example.cw1;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginPageController {
    public Button loginButton;
    public Button registerButton;
    public PasswordField logInPassword;
    public TextField logInUserName;
    public Label failureMessage;
    private Stage stage;
    private Scene scene;
    private Parent parent;

    private String LogIn_username;
    private String LogIn_password;

    String url = "jdbc:mysql://localhost:3306/news";
    String user_db = "root";
    String password = "Dulina@123";



    public void switchToRegisterPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RegisterPage.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMainPage(ActionEvent event, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent root = loader.load();

        HomeController homeController =  loader.getController();
        homeController.setUser(user);

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void login(ActionEvent actionEvent) throws IOException, SQLException {
        LogIn_username = logInUserName.getText();
        LogIn_password = logInPassword.getText();

        boolean validity = validate(LogIn_username,LogIn_password);
        if(validity){
            List<String> userDetails = getUserAttributes(LogIn_username);
            User user = new User(LogIn_username,LogIn_password,userDetails.get(2),userDetails.get(0),userDetails.get(1));
            switchToMainPage(actionEvent,user);
        }

    }

    public boolean validate(String username, String password) throws IOException {
        boolean isExistingUser = userNameCheck(username);
        if(isExistingUser){
            String savedPassword = passwordChecker(username);
            if(savedPassword.equals(password)){
                return true;
            }else {
                failureMessage.setText("Incorrect Password. Please Try Again");
            }

        }else {
            System.out.println("Not a registered user. Please register into the system.");
            failureMessage.setText("Not a registered user. Please register into the system");
        }
        return false;

    }

    public boolean userNameCheck(String username){
        String query = "SELECT * FROM user_final WHERE user_name = ?";
        boolean result = false;

        try(Connection connection = DriverManager.getConnection(url,user_db,password)) {
            PreparedStatement stmt = connection.prepareStatement((query));
            stmt.setString(1,username);

            ResultSet resultSet = stmt.executeQuery();

            result = resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public String passwordChecker(String username){
        String query = "SELECT password from user_final WHERE user_name = ?";
        String result = null;
        try(Connection connection = DriverManager.getConnection(url,user_db,password)) {
            PreparedStatement stmt = connection.prepareStatement((query));
            stmt.setString(1,username);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()){
                result = resultSet.getString("password");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<String> getUserAttributes(String username) throws SQLException {

        List<String> result = new ArrayList<>();

        String query = "Select first_name,last_name,email FROM user_final WHERE user_name = ?";
        try(Connection connection = DriverManager.getConnection(url,user_db,password)) {
            PreparedStatement stmt = connection.prepareStatement((query));
            stmt.setString(1,username);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()){

                result.add(resultSet.getString("first_name"));
                result.add(resultSet.getString("last_name"));
                result.add(resultSet.getString("email"));
            }

        return result;


    }

}}

