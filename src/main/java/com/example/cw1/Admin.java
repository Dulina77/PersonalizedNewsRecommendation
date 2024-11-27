package com.example.cw1;

public class Admin extends User{

    public Admin(String username, String password) {
        super(username, password);
    }
    public Admin(String username, String password,String email, String firstName, String lastName){
        super(username, password, email, firstName, lastName);

    }

}
