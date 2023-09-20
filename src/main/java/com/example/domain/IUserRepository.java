package com.example.domain;

// import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    // public List<User> allUsers(int offset, int size);
    public Optional<User> getUserById(Long id);
    // public Optional<User> update(User user);
    public void createUser(User user);
    // public boolean deleteProductById(Long id);
    // public void deleteAllProducts();
}
