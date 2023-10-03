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
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.example.application.usecases.user.CreateUserUseCase;
import com.example.application.usecases.user.GetUserByIdUseCase;
import com.example.application.usecases.user.LoginUseCase;
import com.example.domain.IUserRepository;
import com.example.domain.User;
import com.example.infrastructure.keycloak.LoginProxy;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class UserServiceTest {
    
    @InjectMock
    private IUserRepository userRepository;

    // @InjectMock
    // private LoginProxy loginProxy;

    @InjectMock
    private Keycloak keycloak;

    @Inject
    private GetUserByIdUseCase getUserByIdUseCase;
    @Inject 
    private CreateUserUseCase createUserUseCase;
    @Inject 
    private LoginUseCase loginUseCase;

    private ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

    @BeforeEach
    void setup() {
        // List<User> returnedList = new ArrayList<User>();
        UserRepresentation testUser = new UserRepresentation();
        testUser.setUsername("name last");
        testUser.setEmail("notused@example.em");
        testUser.setFirstName("name");
        testUser.setLastName("last");
        User user2 = new User(2L, "whatever", "empty", "good@example.em");
        when(userRepository.getUserById(1L)).thenReturn(Optional.empty());        
        when(userRepository.getUserById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.isEmailUsed("good@example.em")).thenReturn(true);
        when(userRepository.isEmailUsed("notused@example.em")).thenReturn(false);   
        when(keycloak.realm("Demo").users().create(testUser)).thenReturn(Response.ok().build());
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
        User user1 = new User(1L, "name", "last", "notused@example.em");
        List<String> roles = List.of("user");
        // when
        createUserUseCase.createUser(user1, roles);
        // then
        verify(keycloak).realm("Demo").users().create(Mockito.any(UserRepresentation.class));
        verify(userRepository).createUser(userArgumentCaptor.capture());

        User passedUser = userArgumentCaptor.getValue();

        assertEquals(passedUser, user1);
    }

    // @Test
    // public void testCreateUserNotOk() {
    //     // given
    //     User user1 = new User(1L, "test1", "test1 last", "good@example.em");
    //     // when
    //     createUserUseCase.createUser(user1);
    //     // then
    //     verify(userRepository).isEmailUsed(user1.getEmail());
    //     verify(userRepository, never()).createUser(Mockito.any(User.class));
    // }

}
