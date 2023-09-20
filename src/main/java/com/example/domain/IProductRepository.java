package com.example.domain;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    public List<Product> allProducts(int offset, int size);
    public Optional<Product> getProductById(Long id);
    public Optional<Product> update(Product product);
    public void createProduct(Product product);
    public boolean deleteProductById(Long id);
    public void deleteAllProducts(); // for test  purposes
}
