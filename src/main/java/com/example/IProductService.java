package com.example;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<Product> getProducts(int offset, int size);
    Optional<Product> getProductById(Long id);
    void newProduct(Product product);
    Optional<Product> udpateProduct(Long id, Product product);
    boolean deleteProduct(Long id);
}