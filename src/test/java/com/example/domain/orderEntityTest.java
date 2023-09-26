package com.example.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;


import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class orderEntityTest {
    
    @Inject
    private TestValidator validator;

    @Test
    public void testOrderInitialization() {
        Order order = new Order();

        assertEquals(null, order.getUserId());
        assertEquals(new ArrayList<Long>(), order.getProductId());
        assertEquals(null, order.getId());        
    }
    @Test
    public void testOrderInitializationAllArgs() {
        List<Long> productIds = new ArrayList<>();
        productIds.add(1L);
        Order order = new Order(2L, productIds);
        assertEquals(2L, order.getUserId());
        assertEquals(1L, order.getProductId().get(0));
    }

    @Test
    public void testOrderValidationNoUserId() {
        List<Long> productIds = new ArrayList<>();
        productIds.add(1L);
        Order order = new Order(null, productIds);

        List<String> violations = validator.violations(order);
        assertEquals("user id must be provided", violations.get(0));
    }

    @Test 
    public void testOrderValidationNoProductIds() {
        Order order = new Order(2L, null);
        List<String> violations = validator.violations(order);
        assertEquals("products cannot be empty", violations.get(0));
    }

    @Test
    public void testOrderValidationOk() {
        List<Long> productIds = new ArrayList<>();
        productIds.add(1L);
        Order order = new Order(2L, productIds);
        List<String> violations = validator.violations(order);
        assertEquals(0, violations.size());
    }
}
