package com.example.application.usecases;

import java.util.Optional;

import com.example.domain.User;

public interface GetUserByIdUseCase {
    Optional<User> getUserById(Long id);
}
