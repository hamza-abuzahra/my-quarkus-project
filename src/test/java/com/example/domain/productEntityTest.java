package com.example.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;


@QuarkusTest
public class productEntityTest {
    
    @Inject
    private testValidator validator;

    @Test
    public void testProductInitialization() {
        Product product = new Product(1L, "apple", "red apple", 12f);

        // check entities attributes
        assertEquals(1L, product.getId());
        assertEquals("apple", product.getName());
        assertEquals("red apple", product.getDescribtion());
        assertEquals(12f, product.getPrice());
        assertEquals(new ArrayList<String>(), product.getImageIds());
    }
    @Test
    public void testProductConstructorNoId() {
        Product product = new Product("apple", "red apple", 12f);

        // check entities attributes
        assertEquals(null, product.getId());
        assertEquals("apple", product.getName());
        assertEquals("red apple", product.getDescribtion());
        assertEquals(12f, product.getPrice());
        assertEquals(new ArrayList<String>(), product.getImageIds());
    }

    @Test 
    public void testProductValidationMissingName() {
        Product missingNameProduct = new Product(1L, "", "good product", 12.4f);
        List<String> violations = validator.violations(missingNameProduct);
        assertFalse(violations.isEmpty());
        assertEquals("Product name cannot be blank", violations.get(0));
    }
    @Test 
    public void testProductValidationInvalidPrice() {
        Product priceIsZeroProduct = new Product(1L, "product", "good product", 0f);
        List<String> violations = validator.violations(priceIsZeroProduct);
        assertFalse(violations.isEmpty());
        assertEquals("Product price must be positive", violations.get(0));
    }
    @Test 
    public void testProductValidationOk() {
        Product okProduct = new Product(3L, "product", "good product", 10f);
        List<String> violations = validator.violations(okProduct);
        assertTrue(violations.isEmpty());
    }
}
