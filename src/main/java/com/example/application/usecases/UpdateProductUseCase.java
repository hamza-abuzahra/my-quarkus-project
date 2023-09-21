package com.example.application.usecases;

import java.util.Optional;

import com.example.domain.Product;

public interface UpdateProductUseCase {
    Optional<Product> updateProduct(Long id, Product product);
}
