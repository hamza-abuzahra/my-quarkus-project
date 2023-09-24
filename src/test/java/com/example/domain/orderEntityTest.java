package com.example.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class orderEntityTest {
    
    @Inject
    private testValidator validator;

    @Test
    public void testOrderInitialization() {
        Order order = new Order();

        // check entities attributes
    }
}
