package com.example.application.usecases.user;

import jakarta.validation.constraints.NotEmpty;

public class UserCredentialsRequest {
    @NotEmpty
    private String Username;
    @NotEmpty
    private String Password;
    public String getUsername() {
        return Username;
    }
    public void setUsername(String username) {
        Username = username;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }
}
