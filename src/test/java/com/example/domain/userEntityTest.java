package com.example.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class userEntityTest {
    
    @Inject
    private testValidator validator;

    @Test
    public void testUserInitialization() {
        User user = new User(1L, "hamza", "abuzahra", "example@email.co");
        // check entities attributes
        assertEquals(1L, user.getId());
        assertEquals("hamza", user.getFname());
        assertEquals("abuzahra", user.getLname());
        assertEquals("example@email.co", user.getEmail());
    }

    @Test
    public void testUserValidationNameNotProvided() {
        User noNameUser = new User(2L, "", "whatever", "example@email.co");

        ArrayList<String> violations = validator.violations(noNameUser);
        assertEquals("User name can not be blank", violations.get(0));
    }

    @Test
    public void testUserValidationEmailNotValid() {
        User invalidEmailUser = new User(1L, "hamza", "", "example");
        ArrayList<String> violations = validator.violations(invalidEmailUser);
        assertEquals("email not valid", violations.get(0));
    }

    @Test
    public void testUserValidationEmailEmpty() {
        User invalidEmailUser = new User(1L, "hamza", "", "");
        ArrayList<String> violations = validator.violations(invalidEmailUser);
        assertEquals("email not provided", violations.get(0));
    }

    @Test
    public void testUserValidationOk() {
        User invalidEmailUser = new User(1L, "hamza", "", "example@email.com");
        ArrayList<String> violations = validator.violations(invalidEmailUser);
        assertEquals(0, violations.size());
    }
}
