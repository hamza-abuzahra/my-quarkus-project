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
        User user = null;
        try {
            user = mapJpaToDomain(findById(id));
        } catch (Exception e){
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    @Transactional
    public void createUser(User user) {
        persist(mapDomainToJpa(user));
    }

    @Override
    public boolean isEmailUsed(String email) {
        List<JpaUser> jpaUser = find("email = ?1", email).list();
        if (jpaUser.size() > 0) {
            return true;
        }
        return false;
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
