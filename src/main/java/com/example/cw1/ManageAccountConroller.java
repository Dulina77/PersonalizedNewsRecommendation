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
import java.sql.SQLException;
import java.util.Objects;

public class ManageAccountConroller {
    @FXML
    private Label Message;
    @FXML
    private TextField Password;
    @FXML
    private TextField FirstName;
    @FXML
    private TextField LastName;
    @FXML
    private TextField Email;
    @FXML
    private Label username;
    @FXML
    private Button Backbutton;
    @FXML
    private Button UpdateFirstNameButton;
    @FXML
    private Button UpdateLastNameButton;
    @FXML
    private Button UpdateEmailButton;
    @FXML
    private Button UpdatePasswordButton;

    private User user;

    public void setUser(User user){
        this.user = user;
        setValues(user);
    }

    public void setValues(User user){
        username.setText(user.getUserName());
        FirstName.setText(user.getFirstName());
        LastName.setText(user.getLastName());
        Email.setText(user.getEmail());
        Password.setText(user.getPassword());
    }

    public void UpdateFirstName() throws SQLException {
        String UpdatedFirstName = FirstName.getText();
        if(!Objects.equals(UpdatedFirstName, user.getFirstName())){
            DataBaseHandler.updateFirstName(UpdatedFirstName,user);
            Message.setText("Successfully Updated First Name");
            user.setFirstName(UpdatedFirstName);
        }else {
            Message.setText("No changes in the First Name");
        }
    }

    public void UpdateLastName() throws SQLException {
        String UpdatedLastName = LastName.getText();
        if(!Objects.equals(UpdatedLastName, user.getLastName())){
            DataBaseHandler.updateLastName(UpdatedLastName,user);
            Message.setText("Successfully Updated Last Name");
            user.setLastName(UpdatedLastName);
        }else {
            Message.setText("No changes in the Last Name");
        }
    }

    public void UpdateEmail() throws SQLException {
        String UpdatedEmail = Email.getText();
        if(!Objects.equals(UpdatedEmail, user.getEmail())){
            DataBaseHandler.updateEmail(UpdatedEmail,user);
            Message.setText("Successfully Updated Email");
            user.setEmail(UpdatedEmail);
        }else {
            Message.setText("No changes in the Email");
        }
    }

    public void UpdatePassword() throws SQLException {
        String UpdatedPassword = Password.getText();
        if(!Objects.equals(UpdatedPassword, user.getPassword())){
            DataBaseHandler.updatePassword(UpdatedPassword,user);
            Message.setText("Successfully Updated Password");
            user.setPassword(UpdatedPassword);
        }else {
            Message.setText("No changes in the Password");
        }
    }


    public void switchToHomePage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Scene scene = new Scene(loader.load());

        HomeController controller = loader.getController();
        controller.setUser(user);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
