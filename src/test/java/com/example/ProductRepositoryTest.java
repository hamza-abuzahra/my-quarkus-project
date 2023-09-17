package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
public class ProductRepositoryTest {

    @Inject
    private IProductRepository repository;

    @AfterEach
    @Transactional
    void tearDown() {
        repository.deleteAllProducts();
    }
    @Test
    @Transactional
    public void testGetAllProducts() {
        // given
        Product product = new Product("apple", "a red delicious apple", 12.3f);
        Product product2 = new Product("table", "big table for 6", 120.99f);
        Product product3 = new Product("car", "very fast car", 15000.7f);
        repository.createProduct(product);        
        repository.createProduct(product2);
        repository.createProduct(product3);

        // when
        List<Product> res = repository.allProducts(0, 2);

        // then
        assertEquals(res.get(0).getName(), "table");
        assertEquals(res.get(1).getName(), "car");
        assertEquals(res.size(), 2);
    }

    @Test
    public void testGetAllProductsEmpty() {
        // given
        // empty database, no entries made beforehand 
        
        // when 
        List<Product> res = repository.allProducts(0, 2);

        // then
        assertEquals(0, res.size());
    }

    @Test
    @Transactional
    public void testGetProductByIdOk() {
        // given
        Product product = new Product("car", "very fast car", 15000.7f);
        repository.createProduct(product);

        // when
        Optional<Product> resOptional = repository.getById(1L);
        
        // then
        assertNotNull(resOptional);
        Product res = resOptional.get();
        assertEquals(res.getName(), "car");
    }

    @Test
    @Transactional
    public void testGetProductByIdNotExist() {
        // given
        // empty db, no entry given beforehand

        // when
        Optional<Product> resOptional = repository.getById(1L);
        
        // then
        assertTrue(resOptional.isEmpty());
    }

    @Test
    @Transactional
    public void testUpdateProductInfoOk() {
        // given
        Product product = new Product("car", "very fast car", 15000.7f);
        repository.createProduct(product);

        // when
        Product updatedProduct = new Product(2L, "car", "very nice car", 12000.0f);
        Optional<Product> resOptional = repository.update(updatedProduct);

        // then
        assertNotNull(resOptional.get());
        Product res = resOptional.get();
        assertEquals(res.getDescribtion(), "very nice car");
    }

    @Test
    public void testUpdateNonExistingProduct() {
        // given
        // when
        Product updatedProduct = new Product(15L, "car", "beautiful car", 12f);
        Optional<Product> resOptional = repository.update(updatedProduct);

        // then
        assertTrue(resOptional.isEmpty());
        Optional<Product> resOptional2 = repository.getById(3L);
        assertTrue(resOptional2.isEmpty());
    }

    @Test
    @Transactional
    public void testCreateProduct() {
        // given
        Product newProduct = new Product("new product", "new product", 23.8f);
        // when 
        repository.createProduct(newProduct);

        // then
        assertEquals(repository.allProducts(0, 2).get(0).getName(), "new product");
    }

    @Test
    @Transactional
    public void testDeleteProduct() {
        // given
        Product newProduct = new Product("new product", "new product", 23.8f);
        repository.createProduct(newProduct);
        // when 
        Long idToDelete = repository.allProducts(0, 2).get(0).getId();
        repository.deleteProductById(idToDelete);
        // then
        assertThrows(NoSuchElementException.class, () -> repository.getById(idToDelete).get());
    }
}
