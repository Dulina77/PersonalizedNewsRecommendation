package com.example.cw1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
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
    public Label Message2;
    @FXML
    public TextField firstNameField;

    @FXML
    public TextField lastNameField;
    public Label systemResponse;
    private Stage stage;
    private Scene scene;
    private Parent parent;

    String userId;
    String FirstName;
    String LastName;
    String UserName;
    String eMail;
    String User_password;
    ArrayList<String> preferences = new ArrayList<>();

    public static final String url = "jdbc:mysql://localhost:3306/news";
    public static final String user = "root";
    public static final String password = "Dulina@123";

    @FXML
    private TextField RegistrationUserName;

    @FXML
    private TextField RegistrationEmail;

    @FXML
    private TextField RegistrationPassword;

    @FXML
    private CheckBox Technology;
    @FXML
    private CheckBox Health;
    @FXML
    private CheckBox Sports;
    @FXML
    private CheckBox Politics;
    @FXML
    private CheckBox Business;

    @FXML
    private Label Message;


    public void switchToLoginPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void register(ActionEvent event) throws IOException {
        UserName = RegistrationUserName.getText();
        User_password = RegistrationPassword.getText();
        eMail = RegistrationEmail.getText();
        FirstName = firstNameField.getText();
        LastName = lastNameField.getText();


        validate(UserName,User_password,eMail,FirstName,LastName,event);

    }

    public void validate(String UserName, String User_password, String eMail, String FirstName, String LastName, ActionEvent actionEvent) throws IOException {
        boolean isExistingUser = userNameCheck(UserName);
        if(isExistingUser){
            systemResponse.setText("The username is already taken");
        }else {
            if(!eMailChecker(eMail)){
                systemResponse.setText("Invalid Email Address");
            }else {
                User user = new User(UserName,User_password,eMail,FirstName,LastName);
                DataBase.insertUser(user);
                systemResponse.setText("User Registration Successful");
            }

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

    public boolean eMailChecker(String email) {
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

}
