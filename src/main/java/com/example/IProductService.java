package com.example;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

import javax.naming.directory.InvalidAttributesException;

public interface IProductService {

    List<Product> getProducts(int offset, int size);
    Optional<Product> getProductById(Long id);
    void newProduct(Product product) throws InvalidAttributesException;
    Product udpateProduct(Long id, Product product) throws InvalidParameterException;
    boolean deleteProduct(Long id);
}