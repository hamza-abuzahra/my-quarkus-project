package com.example.application.usecases.product;

import com.example.domain.Product;

public interface DeleteProductImagesUseCase {
    boolean deleteLinkedImages(Product product);

}
