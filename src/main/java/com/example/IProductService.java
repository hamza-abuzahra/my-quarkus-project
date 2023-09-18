package com.example;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;


public interface IProductService {

    List<Product> getProducts(int offset, int size);
    Optional<Product> getProductById(Long id);
    void newProduct(@Valid @ConvertGroup(from=Default.class, to=ValidationGroups.Post.class) Product product);
    Optional<Product> udpateProduct(Long id, @Valid @ConvertGroup(from=Default.class, to=ValidationGroups.Put.class) Product product);
    boolean deleteProduct(Long id);
}