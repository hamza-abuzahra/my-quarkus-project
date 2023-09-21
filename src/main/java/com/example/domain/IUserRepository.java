package com.example.domain;

// import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    // public List<User> allUsers(int offset, int size);
    Optional<User> getUserById(Long id);
    boolean isEmailUsed(String email);
    // public Optional<User> update(User user);
    void createUser(User user);
    // public boolean deleteUsertById(Long id);
    // public void deleteAllUsers();
}
