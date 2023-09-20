package com.example.usecases;

import java.util.Optional;

import com.example.domain.Product;

public interface GetProductByIdUseCase {
    Optional<Product> getProductById(Long id);
}
