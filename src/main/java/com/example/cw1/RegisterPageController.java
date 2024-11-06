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
import java.util.ArrayList;
import java.util.List;


public class RegisterPageController {
    public Label Message2;
    private Stage stage;
    private Scene scene;
    private Parent parent;

    String userId;
    String UserName;
    String eMail;
    String password;
    ArrayList<String> preferences = new ArrayList<>();

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

    public void register(ActionEvent event){
        UserName = RegistrationUserName.getText();
        password = RegistrationPassword.getText();
        eMail = RegistrationEmail.getText();

        if(Technology.isSelected()){
            preferences.add("Technology");
        } if (Health.isSelected()) {
            preferences.add("Health");
        } if (Sports.isSelected()) {
            preferences.add("Sports");
        } if (Politics.isSelected()) {
            preferences.add("Politics");
        } if (Business.isSelected()) {
            preferences.add("Business");
        }else {
            Message2.setText("No fields selected");
        }



        User user = new User(RegistrationUserName.getText(),RegistrationEmail.getText(),RegistrationPassword.getText(),preferences);
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        System.out.println(user.preferences);

        DataBase.insertUser(user);


    }

}
