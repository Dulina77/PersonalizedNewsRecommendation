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
    String user = "root";
    String password = "Dulina@123";



    public void switchToRegisterPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RegisterPage.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMainPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void login(ActionEvent actionEvent) throws IOException {
        LogIn_username = logInUserName.getText();
        LogIn_password = logInPassword.getText();

        validate(LogIn_username,LogIn_password,actionEvent);
    }

    public void validate(String username, String password, ActionEvent actionEvent) throws IOException {
        boolean isExistingUser = userNameCheck(username);
        if(isExistingUser){
            String savedPassword = passwordChecker(username);
            if(savedPassword.equals(password)){
                User user = new User(username,password);
                switchToMainPage(actionEvent);
            }else {
                failureMessage.setText("Incorrect Password. Please Try Again");
            }

        }else {
            System.out.println("Not a registered user. Please register into the system.");
            failureMessage.setText("Not a registered user. Please register into the system");
        }

    }

    public boolean userNameCheck(String username){
        String query = "SELECT * FROM user_final WHERE user_name = ?";
        boolean result = false;

        try(Connection connection = DriverManager.getConnection(url,user,password)) {
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
        try(Connection connection = DriverManager.getConnection(url,user,password)) {
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
    }}
