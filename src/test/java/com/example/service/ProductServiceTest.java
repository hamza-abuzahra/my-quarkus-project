package com.example.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.example.application.usecases.product.CreateProductUseCase;
import com.example.application.usecases.product.DeleteProductUseCase;
import com.example.application.usecases.product.GetProductByIdUseCase;
import com.example.application.usecases.product.GetProductsUseCase;
import com.example.application.usecases.product.ProductCountUseCase;
import com.example.application.usecases.product.SaveImageProductUseCase;
import com.example.application.usecases.product.UpdateProductUseCase;
import com.example.domain.IProductRepository;
import com.example.domain.ImageSaveService;
import com.example.domain.Product;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
public class ProductServiceTest {

    @InjectMock
    private IProductRepository productRepository;
    @InjectMock
    private ImageSaveService imageSaveService;
    @Inject
    private GetProductsUseCase getProductsUseCase;
    @Inject
    private ProductCountUseCase productCountUseCase;
    @Inject
    private GetProductByIdUseCase getProductByIdUseCase;
    @Inject
    private CreateProductUseCase createProductUseCase;
    @Inject
    private UpdateProductUseCase updateProductUseCase;
    @Inject
    private DeleteProductUseCase deleteProductUseCase;
    @Inject 
    private SaveImageProductUseCase saveImageProductUseCase;


    private ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);


    @BeforeEach
    void setup() {
        List<Product> returnedList = new ArrayList<Product>();
        returnedList.add(0, new Product("apple", "red", 15f));
        returnedList.add(1, new Product("table", "dinning table", 2333f));
        when(productRepository.allProducts(0, 2)).thenReturn(returnedList);        
        when(productRepository.allProducts(0, 4)).thenReturn(new ArrayList<Product>());
        when(productRepository.getProductById(1L)).thenReturn(Optional.empty());        
        when(productRepository.getProductById(2L)).thenReturn(Optional.of(new Product(2L, "table", "dinning table", 2333f)));
        when(productRepository.update(new Product(2L,"object", "desc", 1.1f))).thenReturn(Optional.of(new Product("object", "desc", 1.1f)));
        when(productRepository.update(new Product(1L,"object", "desc", 1.1f))).thenReturn(Optional.empty());
        when(productRepository.deleteProductById(1L)).thenReturn(false);
        when(productRepository.deleteProductById(2L)).thenReturn(true);
        when(productRepository.allProductsCount()).thenReturn(2);
        when(imageSaveService.deleteLinkedImages(Mockito.any(Product.class), Mockito.any(String.class))).thenReturn(true);
        when(imageSaveService.saveImages(Mockito.anyList(), Mockito.any(String.class))).thenReturn(List.of("hi", "hello"));
    }
    
    @Test
    public void testGetProductsOk() {
        // when
        assertEquals(2, getProductsUseCase.getProducts(0, 2).size());        
        // then
        verify(productRepository).allProducts(0, 2);
    }

    @Test
    public void testGetProductsEmpty() {
        // when
        assertEquals(0, getProductsUseCase.getProducts(0, 4).size());        
        // then
        verify(productRepository).allProducts(0, 4);
    }


    @Test 
    public void testProductCount() {
        assertEquals(2, productCountUseCase.productCount());

        verify(productRepository).allProductsCount();
    }

    @Test
    public void testGetProductByIdOk() {
        // when // then
        assertEquals("table", getProductByIdUseCase.getProductById(2L).get().getName());

        verify(productRepository).getProductById(2L);
    }

    @Test
    public void testGetProductByIdNotExists() {
        // when // then
        Optional<Product> resOptional = getProductByIdUseCase.getProductById(1L); 

        assertTrue(resOptional.isEmpty());

        verify(productRepository).getProductById(1L);
    }

    @Test
    @Transactional
    public void testNewProductOk() {
        // given
        Product goodNewProduct = new Product("good", "great", 12f);
        // when // then
        assertDoesNotThrow(() -> createProductUseCase.createProduct(goodNewProduct));
        

        verify(productRepository).createProduct(productArgumentCaptor.capture());

        Product capturedProduct = productArgumentCaptor.getValue();
        assertEquals(capturedProduct, goodNewProduct);
    }

    @Test
    @Transactional
    public void testUpdateProductOk() {
        // given
        Product updateProduct = new Product("object", "desc", 1.1f);
        // when // then
        updateProductUseCase.updateProduct(2L, updateProduct);

        verify(productRepository).update(productArgumentCaptor.capture());

        Product capturedProduct = productArgumentCaptor.getValue();

        assertEquals(capturedProduct.getName(), updateProduct.getName());
    }

    @Test
    @Transactional
    public void testUpdateProductIdNotFound() {
        // given
        Product updateProduct = new Product("object", "desc", 1.1f);
        // when // then
        updateProductUseCase.updateProduct(1L, updateProduct);

        verify(productRepository, never()).update(Mockito.any(Product.class));
    }
    
    @Test
    @Transactional
    public void testDeleteExists() {
        assertFalse(deleteProductUseCase.deleteProduct(1L));
        verify(productRepository).getProductById(1L);
        verify(productRepository, never()).deleteProductById(1L);
    }

    @Test
    @Transactional
    public void testDeleteDoesNotExist() {
        assertTrue(deleteProductUseCase.deleteProduct(2L));
        verify(productRepository).getProductById(2L);
        verify(productRepository).deleteProductById(2L);
        verify(imageSaveService).deleteLinkedImages(Mockito.any(Product.class), Mockito.anyString());
    }
    
    @Test
    public void testSaveImage() {
        File file = new File("src/main/resources/META-INF/resources/images/26a891c5-2c82-433a-8e14-c92b0d724720.jpg");
        saveImageProductUseCase.saveImages(List.of(file));
        verify(imageSaveService).saveImages(Mockito.any(), Mockito.any(String.class));
    }
 
}
