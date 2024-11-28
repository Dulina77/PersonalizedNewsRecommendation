package com.example.cw1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    @FXML
    private Button adminButton;
    private Stage stage;
    private Scene scene;
    private Parent parent;

    private String LogIn_username;
    private String LogIn_password;


    public void switchToRegisterPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RegisterPage.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAdminPage(ActionEvent event, Admin admin) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminPage.fxml"));
        Parent root = loader.load();

        AdminPageController homeController =  loader.getController();
        homeController.setAdmin(admin);

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
            List<String> userDetails = DataBaseHandler.getUserAttributes(LogIn_username);
            User user = new User(LogIn_username,LogIn_password,userDetails.get(2),userDetails.get(0),userDetails.get(1));
            switchToMainPage(actionEvent,user);
        }
    }

    public boolean validate(String username, String password) throws IOException {
        boolean isExistingUser = DataBaseHandler.userNameCheck(username);
        if(isExistingUser){
            String savedPassword = DataBaseHandler.passwordChecker(username);
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



    public void loginAsAdmin(ActionEvent actionEvent) throws IOException, SQLException {
        LogIn_username = logInUserName.getText();
        LogIn_password = logInPassword.getText();

        boolean validity = validateAdmin(LogIn_username,LogIn_password);
        if(validity){
            List<String> AdminDetails = DataBaseHandler.getAdminAttributes(LogIn_username);
            Admin admin = new Admin(LogIn_username,LogIn_password,AdminDetails.get(2),AdminDetails.get(0),AdminDetails.get(1));
            switchToAdminPage(actionEvent,admin);
        }

    }

    public boolean validateAdmin(String username, String password) throws IOException {
        boolean isExistingUser = DataBaseHandler.userNameCheckAdmin(username);
        if(isExistingUser){
            String savedPassword = DataBaseHandler.passwordCheckerAdmin(username);
            if(savedPassword.equals(password)){
                return true;
            }else {
                failureMessage.setText("Incorrect Password. Please Try Again");
            }

        }else {
            System.out.println("Not a registered Admin. Please register into the system.");
            failureMessage.setText("Not a registered Admin. Please register into the system");
        }
        return false;

    }
}

