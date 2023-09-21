package com.example.application.usecases.user;

import java.util.Optional;

import com.example.domain.IUserRepository;
import com.example.domain.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService implements GetUserByIdUseCase, CreateUserUseCase {

    @Inject
    private IUserRepository userRepository;

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    @Transactional
    public boolean createUser(User user) {
        if (userRepository.isEmailUsed(user.getEmail())) {
            return false;
        }
        userRepository.createUser(user);
        return true;
    }
    
}
