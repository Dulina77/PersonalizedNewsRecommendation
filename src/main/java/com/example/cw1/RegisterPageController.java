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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.regex.Pattern;


public class RegisterPageController {
    @FXML
    private Label Message2;
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;
    @FXML
    private Label systemResponse;

    private Stage stage;
    private Scene scene;
    private Parent parent;

    private String userId;
    private String FirstName;
    private String LastName;
    private String UserName;
    private String eMail;
    private String User_password;

    @FXML
    private TextField RegistrationUserName;

    @FXML
    private TextField RegistrationEmail;

    @FXML
    private TextField RegistrationPassword;

    @FXML
    private Label Message;


    public void switchToLoginPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void register(ActionEvent event) throws IOException, SQLException {
        UserName = RegistrationUserName.getText();
        User_password = RegistrationPassword.getText();
        eMail = RegistrationEmail.getText();
        FirstName = firstNameField.getText();
        LastName = lastNameField.getText();

        if(UserName == null || UserName.isEmpty() || User_password == null || User_password.isEmpty() || eMail == null || eMail.isEmpty() || FirstName == null || FirstName.isEmpty() || LastName == null || LastName.isEmpty() ) {
            Platform.runLater(() -> systemResponse.setText("Fill all fields to continue"));
            return;
        }

        Thread registerThread = new Thread(() -> {
            try {
                validate(UserName, User_password, eMail, FirstName, LastName, event);
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> systemResponse.setText("An error occurred. Please try again."));
            }
        });
        registerThread.start();

    }

    public void validate(String UserName, String User_password, String eMail, String FirstName, String LastName, ActionEvent actionEvent) throws SQLException {
        try {
            boolean isExistingUser = DataBaseHandler.userNameCheck(UserName);
            if (isExistingUser) {
                Platform.runLater(() -> systemResponse.setText("The username is already taken"));
                return;
            }
            if (!eMailChecker(eMail)) {
                Platform.runLater(() -> systemResponse.setText("Invalid Email Address"));
                return;
            }
            if (!validatePassword(User_password)) {
                Platform.runLater(() -> systemResponse.setText("Password must be at least 5 characters long"));
                return;
            }

            User user = new User(UserName, User_password, eMail, FirstName, LastName);
            DataBaseHandler.insertUser(user);
            Platform.runLater(() -> systemResponse.setText("User Registration Successful. Please go back to logIn page and log in to the system using your credentials"));

        }catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> systemResponse.setText("An error occurred during registration. Please try again."));
        }
    }


    public boolean eMailChecker(String email) {
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public boolean validatePassword(String password) {
        return password != null && password.length() >= 5;
    }



}
