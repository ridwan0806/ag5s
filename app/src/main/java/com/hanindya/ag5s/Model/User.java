package com.hanindya.ag5s.Model;

public class User {
    String UserId,Email,Password,Username,Branch;

    public User() {
    }

    public User(String userId, String email, String password, String username, String branch) {
        UserId = userId;
        Email = email;
        Password = password;
        Username = username;
        Branch = branch;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
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

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }
}
