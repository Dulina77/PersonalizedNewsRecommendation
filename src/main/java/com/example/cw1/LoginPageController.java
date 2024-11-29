package com.example.cw1;

import javafx.application.Platform;
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


    public void login(ActionEvent actionEvent) {
        LogIn_username = logInUserName.getText();
        LogIn_password = logInPassword.getText();

        Thread loginThread = new Thread(() -> {
            try {
                boolean isValid = validate(LogIn_username, LogIn_password);
                if (isValid) {
                    List<String> userDetails = DataBaseHandler.getUserAttributes(LogIn_username);
                    User user = new User(LogIn_username, LogIn_password, userDetails.get(2), userDetails.get(0), userDetails.get(1));

                    Platform.runLater(() -> {
                        try {
                            switchToMainPage(actionEvent, user);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    Platform.runLater(() -> failureMessage.setText("Invalid username or password. Please try again."));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> failureMessage.setText("An error occurred. Please try again."));
            }
        });

        loginThread.start();
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



    public void loginAsAdmin(ActionEvent actionEvent) {
        LogIn_username = logInUserName.getText();
        LogIn_password = logInPassword.getText();

        if (LogIn_username == null || LogIn_username.isEmpty() || LogIn_password == null || LogIn_password.isEmpty()) {
            failureMessage.setText("Please fill in both username and password.");
            return;
        }

        Thread adminLoginThread = new Thread(() -> {
            try {
                boolean validity = validateAdmin(LogIn_username, LogIn_password);
                if (validity) {
                    List<String> AdminDetails = DataBaseHandler.getAdminAttributes(LogIn_username);
                    Admin admin = new Admin(LogIn_username, LogIn_password, AdminDetails.get(2), AdminDetails.get(0), AdminDetails.get(1));

                    Platform.runLater(() -> {
                        try {
                            switchToAdminPage(actionEvent, admin);
                        } catch (IOException e) {
                            e.printStackTrace();
                            failureMessage.setText("Error navigating to the Admin page.");
                        }
                    });
                } else {
                    Platform.runLater(() -> failureMessage.setText("Invalid admin credentials. Please try again."));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> failureMessage.setText("An error occurred during admin login. Please try again."));
            }
        });
        adminLoginThread.start();
    }

    public boolean validateAdmin(String username, String password) throws IOException {
        boolean isExistingAdmin = DataBaseHandler.userNameCheckAdmin(username);
        if(isExistingAdmin){
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

