package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.example.application.usecases.user.CreateUserUseCase;
import com.example.application.usecases.user.GetUserByIdUseCase;
import com.example.application.usecases.user.LoginUseCase;
import com.example.application.usecases.user.UserCredentialsRequest;
import com.example.domain.IUserRepository;
import com.example.domain.IdentityServiceInterface;
import com.example.domain.User;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class UserServiceTest {
    
    @InjectMock
    private IUserRepository userRepository;

    @InjectMock
    private IdentityServiceInterface identityService;

    @Inject
    private GetUserByIdUseCase getUserByIdUseCase;
    @Inject 
    private CreateUserUseCase createUserUseCase;
    @Inject 
    private LoginUseCase loginUseCase;

    private ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
    private ArgumentCaptor<List<String>> rolesArgumentCaptor = ArgumentCaptor.forClass((Class<List<String>>) (Class<?>) List.class);

    private User user1 = new User(1L, "name", "last", "notused@example.em");
    private User user2 = new User(2L, "whatever", "empty", "good@example.em");
    
    @BeforeEach
    void setup() {
        // List<User> returnedList = new ArrayList<User>();
        when(userRepository.getUserById(1L)).thenReturn(Optional.empty());        
        when(userRepository.getUserById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.isEmailUsed("good@example.em")).thenReturn(true);
        when(userRepository.isEmailUsed("notused@example.em")).thenReturn(false);
        when(identityService.createUser(user1, List.of("user"))).thenReturn("");
        when(identityService.createUser(user2, List.of("admin"))).thenReturn("{\"error_message\": \"email alreay used\"}");
        when(identityService.getToken("d", "e")).thenReturn("token");
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
    
    @Test
    public void testCreateUserOk() {
        // given
        List<String> roles = List.of("user");
        // when
        String res = createUserUseCase.createUser(user1, roles);
        // then
        verify(identityService).createUser(userArgumentCaptor.capture(), rolesArgumentCaptor.capture());
        User passedUser = userArgumentCaptor.getValue();
        assertEquals(passedUser, user1);
        assertEquals(rolesArgumentCaptor.getValue(), roles);
        assertEquals("", res);
        verify(userRepository).createUser(userArgumentCaptor.capture());
        User passedUser2 = userArgumentCaptor.getValue();
        assertEquals(passedUser2, user1);
    }

    @Test
    public void testCreateUserNotOk() {
        // given
        // when
        String result = createUserUseCase.createUser(user2, List.of("admin"));
        // then
        verify(identityService).createUser(userArgumentCaptor.capture(), rolesArgumentCaptor.capture());
        verify(userRepository, never()).createUser(Mockito.any(User.class));
        assertEquals("{\"error_message\": \"email alreay used\"}", result);
    }

    @Test
    public void testLogin() {
        UserCredentialsRequest userCredentialsRequest = new UserCredentialsRequest();
        userCredentialsRequest.setUsername("d");
        userCredentialsRequest.setPassword("e");
        String token = loginUseCase.login(userCredentialsRequest);
        verify(identityService).getToken("d", "e");
        assertEquals("token", token);
    }
}
