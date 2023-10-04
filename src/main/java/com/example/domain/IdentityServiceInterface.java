package com.example.domain;

import java.util.List;

public interface IdentityServiceInterface {
    public String getToken(String username, String password);
    public String createUser(User user, List<String> roles);
}
