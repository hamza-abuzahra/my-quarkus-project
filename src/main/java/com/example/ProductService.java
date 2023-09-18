package com.example;

import java.util.List;
import java.util.Optional;

import com.example.ValidationGroups.Post;
import com.example.ValidationGroups.Put;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

@ApplicationScoped
public class ProductService implements IProductService {

    private final IProductRepository productRepo;
    // PanacheRepository<Product>

    public ProductService(IProductRepository products){
        this.productRepo = products;
    }

    @Override
    public List<Product> getProducts(int offset, int size) {
        List<Product> productsList = productRepo.allProducts(offset, size);
        return productsList;
    }    

    @Override
    public Optional<Product> getProductById(Long id) {
        Optional<Product> resOptional = productRepo.getById(id);
        return resOptional;
    }

    @Override
    @Transactional
    public void newProduct(@Valid @ConvertGroup(from=Default.class, to=Post.class) Product product){
        productRepo.createProduct(product);
    }

    @Override
    @Transactional
    public Optional<Product> udpateProduct(Long id, @Valid @ConvertGroup(from=Default.class, to=Put.class) Product product){
        product.setId(id);
        Optional<Product> res = productRepo.update(product);
        return res;
    }

    @Override
    @Transactional
    public boolean deleteProduct(Long id){
        return productRepo.deleteProductById(id);
    }

}

