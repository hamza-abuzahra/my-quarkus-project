package com.example.usecases;

import java.util.List;

import com.example.domain.Product;

public interface GetProductsUseCase {
    List<Product> getProducts(int offset, int size);
}
