package com.example.cw1;

import java.util.ArrayList;

public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    ArrayList<String> preferences;

    public User(String name, String email, String password,ArrayList<String> preferences){
        this.name = name;
        this.email = email;
        this.password = password;
        this.preferences = preferences;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(ArrayList<String> preferences) {
        this.preferences = preferences;
    }

    public int getUserId() {
        return userId;
    }
}
