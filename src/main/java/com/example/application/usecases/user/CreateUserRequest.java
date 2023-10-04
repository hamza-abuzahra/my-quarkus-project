package com.example.application.usecases.user;

import java.util.List;

import com.example.domain.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CreateUserRequest {
    @NotNull
    private @Valid User user;
    private List<String> roles;
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
