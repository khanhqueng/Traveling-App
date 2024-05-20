package com.example.uddd.Models;

public class User {
    private int id;
    private String Hoten;
    private String Email;
    private String Password;




    public User(int Id, String hoten, String email, String password) {
        id = Id;
        Hoten = hoten;
        Email = email;
        Password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id= id;
    }

    public String getHoten() {
        return Hoten;
    }

    public void setHoten(String hoten) {
        Hoten = hoten;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
