package com.example.makeday.models;

public class User {

    String user_id;
    String user_name;
    String user_email;
    String user_phone;
    String user_password;
    String user_rePassword;
    String user_profile;

    public User() {
    }

    public User(String user_id, String user_name, String user_email, String user_phone, String user_password, String user_rePassword, String user_profile) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.user_password = user_password;
        this.user_rePassword = user_rePassword;
        this.user_profile = user_profile;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_rePassword() {
        return user_rePassword;
    }

    public void setUser_rePassword(String user_rePassword) {
        this.user_rePassword = user_rePassword;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }
}
