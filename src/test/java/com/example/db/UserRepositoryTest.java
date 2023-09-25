package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.example.domain.IUserRepository;
import com.example.domain.User;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
public class UserRepositoryTest {

    @Inject
    private IUserRepository userRepository;

    @Test
    @Transactional
    public void testCreateUser() {
        // given
        User user = new User(1L, "Hamza", "Abuzahra", "example@email.com");
        // when
        userRepository.createUser(user);
        // then 
        assertEquals(userRepository.getUserById(1L).get().getFname(), "Hamza");
    }

    @Test
    public void testGetUserByIdOk() {
        User user = new User(1L, "Hamza 1", "Abuzahra", "example@email.com");
        userRepository.createUser(user);
        // when
        Optional<User> resOptional = userRepository.getUserById(1L);
        // then
        assertFalse(resOptional.isEmpty());
        assertEquals("Hamza 1", resOptional.get().getFname());
    }
    
    @Test 
    public void testGetUserByIdNotExists() {
        // when
        Optional<User> resOptional = userRepository.getUserById(3L);

        // then
        assertTrue(resOptional.isEmpty());
    }
}
