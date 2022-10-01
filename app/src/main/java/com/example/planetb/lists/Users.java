package com.example.planetb.lists;

import java.util.ArrayList;

public class Users {
    static String firstName, lastName, email, phoneNo, password;

    public Users(String firstName, String lastName, String email, String phoneNo, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
    }

    public Users() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
