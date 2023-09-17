package com.example;

import java.util.List;
import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product>, IProductRepository{
   
    @Override
    public List<Product> allProducts(int offset, int size){
        return this.findAll(Sort.descending("name")).page(Page.of(offset, size)).list();
    }

    @Override
    public Optional<Product> getById(Long id){
        Product product = null;
        try {
            product = this.findById(id);
        } catch (NoResultException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(product);
    }
    
    @Override
    public Optional<Product> update(Product product){
        final Long id = product.getId();
    
        Optional<Product> savedOpt = this.getById(id);
        if (savedOpt.isEmpty()){
            return savedOpt;
        }
        Product saved = savedOpt.get();
        saved.setName(product.getName());
        saved.setPrice(product.getPrice());
        saved.setDescribtion(product.getDescribtion());
        this.persist(saved);
        return Optional.of(saved);
    }

    @Override
    public void createProduct(Product product) {
        this.persist(product);
    }

    @Override
    public boolean deleteProductById(Long id) {
        return this.deleteById(id);
    }

    @Override
    public void deleteAllProducts() {
        this.deleteAll();
    }
}
