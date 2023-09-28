package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.example.domain.IUserRepository;
import com.example.domain.User;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.transaction.annotations.Rollback;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
@Transactional
@Rollback(true)
@TestInstance(Lifecycle.PER_CLASS)
public class UserRepositoryTest {

    @Inject
    private IUserRepository userRepository;

    @BeforeAll
    @Transactional
    void setup() {
        userRepository.deleteAllUsers();
    }
    @AfterEach
    @Transactional
    void tearDown() {
        userRepository.deleteAllUsers();
    }

    @Test
    @Transactional
    public void testCreateUser() {
        // given
        User user = new User(1L, "Hamza", "Abuzahra", "example@email.com");
        // when
        userRepository.createUser(user);
        // then 
        assertEquals(userRepository.getUserById(9L).get().getFname(), "Hamza");
    }
    
    @Test
    @Transactional
    public void testGetUserByIdOk() {
        User user = new User(1L, "Ahmed", "Abuzahra", "example@email.com");
        userRepository.createUser(user);
        // when
        Optional<User> resOptional = userRepository.getUserById(8L);
        // then
        assertFalse(resOptional.isEmpty());
        assertEquals("Ahmed", resOptional.get().getFname());
    }
    
    @Test 
    public void testGetUserByIdNotExists() {
        // when
        Optional<User> resOptional = userRepository.getUserById(3L);

        // then
        assertTrue(resOptional.isEmpty());
    }

    @Test
    @Transactional
    public void testIsEmailUsedOk() {
        // given
        User user = new User(1L, "Abdullah", "Abuzahra", "example@email.com");
        userRepository.createUser(user);
        
        // when
        boolean used = userRepository.isEmailUsed(user.getEmail());
        assertTrue(used);
    }
    
    @Test
    public void testIsEmailUsedNotOk() {
        // given
        
        // when
        boolean used = userRepository.isEmailUsed("notusedemail@example.com");
        assertFalse(used);
    }
}
