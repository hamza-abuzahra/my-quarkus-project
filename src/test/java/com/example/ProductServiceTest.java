package com.example;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.naming.directory.InvalidAttributesException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;

@QuarkusTest
public class ProductServiceTest {

    @InjectMock
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setup() {
        productService = new ProductService(productRepository);
        List<Product> returnedList = new ArrayList<Product>();
        returnedList.add(0, new Product("apple", "red", 15f));
        returnedList.add(1, new Product("table", "dinning table", 2333f));
        when(productRepository.allProducts(0, 2)).thenReturn(returnedList);
        when(productRepository.getById(1L)).thenReturn(Optional.empty());        
        when(productRepository.getById(2L)).thenReturn(Optional.of(new Product("table", "dinning table", 2333f)));
        when(productRepository.update(new Product(2L,"object", "desc", 1.1f))).thenReturn(Optional.of(new Product("object", "desc", 1.1f)));
        when(productRepository.update(new Product(1L,"object", "desc", 1.1f))).thenReturn(Optional.empty());
        when(productRepository.deleteById(1L)).thenReturn(false);
        when(productRepository.deleteById(2L)).thenReturn(true);
    }
    
    @Test
    public void testGetProductsOk() {
        // when
        assertEquals(2, productService.getProducts(0, 2).size());        
        // then
        verify(productRepository).allProducts(0, 2);
    }

    @Test
    public void testGetProductByIdOk() {
        // when
        assertEquals(Optional.empty(), productService.getProductById(1L));        
        assertEquals("table", productService.getProductById(2L).get().getName());

        verify(productRepository).getById(1L);
    }

    @Test
    @Transactional
    public void testNewProduct() {
        // given
        Product newProduct = new Product(3L, "test", "test", 12000.0f);
        Product newProduct2 = new Product(null, "testing no name", 1f);
        Product newProduct3 = new Product("test no price", "testing", 0f);
        Product goodNewProduct = new Product("good", "great", 12f);

        // when 
        assertThrows(InvalidAttributesException.class, () -> productService.newProduct(newProduct), "Id must not be filled");

        assertThrows(InvalidAttributesException.class, () -> productService.newProduct(newProduct2), "Name must be filled");

        assertThrows(InvalidAttributesException.class, () -> productService.newProduct(newProduct3), "Price must be filled");

        assertDoesNotThrow(() -> productService.newProduct(goodNewProduct));
    }

    @Test
    @Transactional
    public void testUpdateProductNotExists() {
        // given
        Product updateProduct = new Product("object", "desc", 1.1f);
        // when
        // then
        assertThrows(InvalidParameterException.class, () -> productService.udpate(1L, updateProduct));
    }
    @Test
    @Transactional
    public void testUpdateProductOk() {
        // given
        Product updateProduct = new Product("object", "desc", 1.1f);
        // when
        // then
        productService.udpate(2L, updateProduct);
        verify(productRepository).update(updateProduct);
    }
    @Test
    @Transactional
    public void testDelete() {
        assertFalse(productRepository.deleteById(1L));
        assertTrue(productRepository.deleteById(2L));
    }

}
