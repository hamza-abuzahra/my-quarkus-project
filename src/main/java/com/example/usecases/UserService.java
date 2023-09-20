package com.example.usecases;

import java.util.Optional;

import com.example.domain.IUserRepository;
import com.example.domain.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService implements GetUserByIdUseCase{

    @Inject
    private IUserRepository userRepository;

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.getUserById(id);
    }
    
}
