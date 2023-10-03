package com.example.application.usecases.user;

import java.util.List;

import com.example.domain.User;

public interface CreateUserUseCase {
    String createUser(User user, List<String> roles);
}
