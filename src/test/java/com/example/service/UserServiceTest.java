package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.example.application.usecases.user.CreateUserUseCase;
import com.example.application.usecases.user.GetUserByIdUseCase;
import com.example.domain.IUserRepository;
import com.example.domain.User;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class UserServiceTest {
    
    @InjectMock
    private IUserRepository userRepository;

    @Inject
    private GetUserByIdUseCase getUserByIdUseCase;
    @Inject 
    private CreateUserUseCase createUserUseCase;

    private ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

    @BeforeEach
    void setup() {
        // List<User> returnedList = new ArrayList<User>();
        User user2 = new User(2L, "whatever", "empty", "good@example.em");
        when(userRepository.getUserById(1L)).thenReturn(Optional.empty());        
        when(userRepository.getUserById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.isEmailUsed("good@example.em")).thenReturn(true);
        when(userRepository.isEmailUsed("notused@example.em")).thenReturn(false);       
    }
    
    @Test
    public void testCreateUserOk() {
        // given
        User user1 = new User(1L, "test1", "test1 last", "notused@example.em");
        // when
        createUserUseCase.createUser(user1);
        // then
        verify(userRepository).isEmailUsed(user1.getEmail());
        verify(userRepository).createUser(userArgumentCaptor.capture());

        User passedUser = userArgumentCaptor.getValue();

        assertEquals(passedUser, user1);
    }

    @Test
    public void testCreateUserNotOk() {
        // given
        User user1 = new User(1L, "test1", "test1 last", "good@example.em");
        // when
        createUserUseCase.createUser(user1);
        // then
        verify(userRepository).isEmailUsed(user1.getEmail());
        verify(userRepository, never()).createUser(Mockito.any(User.class));
    }

    @Test
    public void testGetUserByIdOk() {
        assertEquals("whatever", getUserByIdUseCase.getUserById(2L).get().getFname());

        verify(userRepository).getUserById(2L);
    }

    @Test
    public void testGetUserByIdNotOk() {
        // when // then
        Optional<User> resOptional = getUserByIdUseCase.getUserById(1L); 

        assertTrue(resOptional.isEmpty());

        verify(userRepository).getUserById(1L);
    }
}
