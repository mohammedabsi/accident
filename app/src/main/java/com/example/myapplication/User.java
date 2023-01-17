package com.example.myapplication;

public class User {
    public String  email, password, userName , date;


    public User() {

    }

    public User( String userName, String password, String email , String date) {

        this.userName = userName;
        this.password = password;
        this.email = email;
        this.date = date;



    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
