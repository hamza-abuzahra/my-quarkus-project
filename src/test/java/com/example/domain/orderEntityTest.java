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

        assertEquals(null, order.getUser());
        assertEquals(new ArrayList<Long>(), order.getProducts());
        assertEquals(null, order.getId());        
    }
    @Test
    public void testOrderInitializationAllArgs() {
        Product product = new Product("laptop", "predator gaming laptop", 2311.5f);
        List<Product> products = List.of(product);
        User user = new User("first name", "last name", "good@email.com");
        Order order = new Order(user, products);
        assertEquals("first name", order.getUser().getFname());
        assertEquals("laptop", order.getProducts().get(0).getName());
    }

    @Test
    public void testOrderValidationNoUserId() {
        Product product = new Product("laptop", "predator gaming laptop", 2311.5f);
        List<Product> products = List.of(product);
        Order order = new Order(null, products);

        List<String> violations = validator.violations(order);
        assertEquals("user id must be provided", violations.get(0));
    }

    @Test 
    public void testOrderValidationNoProductIds() {
        User user = new User("first name", "last name", "good@email.com");
        Order order = new Order(user, null);
        List<String> violations = validator.violations(order);
        assertEquals("products cannot be empty", violations.get(0));
    }

    @Test
    public void testOrderValidationOk() {
        Product product = new Product("laptop", "predator gaming laptop", 2311.5f);
        List<Product> products = List.of(product);
        User user = new User("first name", "last name", "good@email.com");
        Order order = new Order(user, products);
        List<String> violations = validator.violations(order);
        assertEquals(0, violations.size());
    }
}
