// package com.example;

// import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.ArgumentCaptor;

// import com.example.domain.IProductRepository;
// import com.example.domain.Product;

// import io.quarkus.test.InjectMock;
// import io.quarkus.test.junit.QuarkusTest;
// import jakarta.transaction.Transactional;

// @QuarkusTest
// public class ProductServiceTest {

//     @InjectMock
//     private IProductRepository productRepository;
//     private IProductService productService;
//     private ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);


//     @BeforeEach
//     void setup() {
//         productService = new ProductService(productRepository);
//         List<Product> returnedList = new ArrayList<Product>();
//         returnedList.add(0, new Product("apple", "red", 15f));
//         returnedList.add(1, new Product("table", "dinning table", 2333f));
//         when(productRepository.allProducts(0, 2)).thenReturn(returnedList);
//         when(productRepository.getById(1L)).thenReturn(Optional.empty());        
//         when(productRepository.getById(2L)).thenReturn(Optional.of(new Product("table", "dinning table", 2333f)));
//         when(productRepository.update(new Product(2L,"object", "desc", 1.1f))).thenReturn(Optional.of(new Product("object", "desc", 1.1f)));
//         when(productRepository.update(new Product(1L,"object", "desc", 1.1f))).thenReturn(Optional.empty());
//         when(productRepository.deleteProductById(1L)).thenReturn(false);
//         when(productRepository.deleteProductById(2L)).thenReturn(true);
//     }
    
//     @Test
//     public void testGetProductsOk() {
//         // when
//         assertEquals(2, productService.getProducts(0, 2).size());        
//         // then
//         verify(productRepository).allProducts(0, 2);
//     }

//     @Test
//     public void testGetProductByIdOk() {
//         // when // then
//         assertEquals("table", productService.getProductById(2L).get().getName());

//         verify(productRepository).getById(2L);
//     }

//     @Test
//     public void testGetProductByIdNotExists() {
//         // when // then
//         Optional<Product> resOptional = productService.getProductById(1L); 

//         assertTrue(resOptional.isEmpty());

//         verify(productRepository).getById(1L);
//     }

//     @Test
//     @Transactional
//     public void testNewProductOk() {
//         // given
//         Product goodNewProduct = new Product("good", "great", 12f);
//         // when // then
//         assertDoesNotThrow(() -> productService.newProduct(goodNewProduct));
        

//         verify(productRepository).createProduct(productArgumentCaptor.capture());

//         Product capturedProduct = productArgumentCaptor.getValue();
//         assertEquals(capturedProduct, goodNewProduct);
//     }

//     @Test
//     @Transactional
//     public void testUpdateProductOk() {
//         // given
//         Product updateProduct = new Product("object", "desc", 1.1f);
//         // when // then
//         productService.udpateProduct(2L, updateProduct);

//         verify(productRepository).update(productArgumentCaptor.capture());

//         Product capturedProduct = productArgumentCaptor.getValue();

//         assertEquals(capturedProduct.getName(), updateProduct.getName());
//     }
    
//     @Test
//     @Transactional
//     public void testDeleteExists() {
//         assertFalse(productService.deleteProduct(1L));
//         verify(productRepository).deleteProductById(1L);
//     }

//     @Test
//     @Transactional
//     public void testDeleteDoesNotExist() {
//         assertTrue(productService.deleteProduct(2L));
//         verify(productRepository).deleteProductById(2L);
//     }


// }
