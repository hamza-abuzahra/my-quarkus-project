package com.example.application.usecases.user;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.example.domain.IUserRepository;
import com.example.domain.IdentityServiceInterface;
import com.example.domain.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService implements GetUserByIdUseCase, CreateUserUseCase, LoginUseCase {

    @Inject
    private IUserRepository userRepository;

    @Inject 
    private IdentityServiceInterface identityService;


    Logger logger = Logger.getLogger(UserService.class.getName());

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    @Transactional
    public String createUser(User user, List<String> roles) {
        String res = identityService.createUser(user, roles);
        if (res != ""){
            return res;
        }
        userRepository.createUser(user);
        return "";
    }

    @Override
    public String login(UserCredentialsRequest credentials) {   
        return identityService.getToken(credentials.getUsername(), credentials.getPassword());
    }
}
