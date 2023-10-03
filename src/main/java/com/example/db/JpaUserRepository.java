package com.example.db;

import java.util.List;
import java.util.Optional;

import com.example.domain.IUserRepository;
import com.example.domain.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class JpaUserRepository implements IUserRepository, PanacheRepository<JpaUser>{


    @Override
    public Optional<User> getUserById(Long id) {
        try {
            JpaUser jpaUser = findById(id);
            User user = new User(jpaUser.getId(), jpaUser.getFname(), jpaUser.getLname(), jpaUser.getEmail());
            return Optional.of(user);
        } catch (Exception e){
            // e.printStackTrace();
        }
        return Optional.empty();    

    }

    @Override
    @Transactional
    public void createUser(User user) {
        JpaUser jpaUser = new JpaUser(user.getFname(), user.getLname(), user.getEmail());
        persist(jpaUser);
    }

    @Override
    public boolean isEmailUsed(String email) {
        List<JpaUser> jpaUser = find("email = ?1", email).list();
        if (jpaUser.size() > 0) {
            return true;
        }
        return false;
    }

    // for testing purposes
    @Override
    @Transactional
    public void deleteAllUsers() {
        deleteAll();
        String sql = "Select setval('jpauser_seq', 1)";
        list(sql);
    }
}
