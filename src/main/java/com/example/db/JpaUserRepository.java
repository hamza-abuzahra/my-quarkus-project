package com.example.db;

import java.util.Optional;

import com.example.domain.IUserRepository;
import com.example.domain.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JpaUserRepository implements IUserRepository, PanacheRepository<JpaUser>{

    @Override
    public Optional<User> getUserById(Long id) {
        User user = null;
        try {
            user = mapJpaToDomain(findById(id));
        } catch (Exception e){
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public void createUser(User user) {
        persist(mapDomainToJpa(user));
    }

    private static JpaUser mapDomainToJpa(User user){
        JpaUser jpaUser = new JpaUser();
        jpaUser.setFname(user.getFname());
        jpaUser.setLname(user.getLname());        
        jpaUser.setEmail(user.getEmail());
        return jpaUser;
    }
    private static User mapJpaToDomain(JpaUser jpaUser){
        User user = new User();
        user.setId(jpaUser.getId());
        user.setFname(jpaUser.getFname());
        user.setLname(jpaUser.getLname());
        user.setEmail(jpaUser.getEmail());
        return user;
    }
}
