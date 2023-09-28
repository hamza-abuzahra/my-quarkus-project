package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.example.domain.IProductRepository;
import com.example.domain.Product;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.transaction.annotations.Rollback;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
@Transactional
@Rollback(true)
@TestInstance(Lifecycle.PER_CLASS)
public class ProductRepositoryTest {

    @Inject
    private IProductRepository repository;

    Logger logger = Logger.getLogger(ProductRepositoryTest.class.getName());

    @BeforeAll
    @Transactional
    void setup () {
        repository.deleteAllProducts();
    }
    @AfterAll
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
        assertEquals(res.get(0).getName(), "apple");
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
    public void testGetProductCount() {
        // given
        Product product = new Product(1L, "apple", "a red delicious apple", 12.3f);
        Product product2 = new Product("table", "big table for 6", 120.99f);
        Product product3 = new Product("car", "very fast car", 15000.7f);
        repository.createProduct(product);        
        repository.createProduct(product2);
        repository.createProduct(product3);

        // when
        int productCount = repository.allProductsCount();

        // then
        assertEquals(3, productCount);
    }
    
    @Test
    public void testGetProductCountEmpty() {
        // given

        // when
        int productCount = repository.allProductsCount();

        // then
        assertEquals(0, productCount);
    }

    @Test
    @Transactional
    public void testGetProductByIdOk() {
        // given
        Product product = new Product("car", "very fast car", 15000.7f);
        repository.createProduct(product);

        // when
        Optional<Product> resOptional = repository.getProductById(11L);
        
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
        Optional<Product> resOptional = repository.getProductById(1L);
        
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
        Product updatedProduct = new Product(12L, "car", "very nice car", 12000.0f);
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
    }

    @Test
    @Transactional
    public void testCreateProduct() {
        // given
        Product newProduct = new Product("new product", "new product", 23.8f);
                
        // logger.info(repository.allProducts(0,10).get(0).getId()+"");
        // logger.info(repository.allProducts(0, 10).size()+"");
        // when 
        repository.createProduct(newProduct);

        // then
        assertEquals(repository.getProductById(13L).get().getName(), "new product");
    }

    @Test
    @Transactional
    public void testDeleteProduct() {
        // given
        Product newProduct = new Product("new product", "new product", 23.8f);
        repository.createProduct(newProduct);
        // when 
        Long idToDelete = repository.allProducts(0, 2).get(0).getId();
        assertTrue(repository.deleteProductById(idToDelete));
        // then
        assertThrows(NoSuchElementException.class, () -> repository.getProductById(idToDelete).get());
    }
}
