package com.example.cw1;

import java.util.ArrayList;

public class User {
    private String FirstName;
    private String LastName;
    private String userName;
    private String email;
    private String password;
    private UserProfile profile;


    public User(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    public User(String userName, String password, String email, String firstName, String lastName){
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.profile = new UserProfile();
    }


    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
}
