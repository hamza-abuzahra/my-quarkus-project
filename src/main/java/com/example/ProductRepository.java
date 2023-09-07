package com.example;

import java.util.List;
import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product>{
    public List<Product> allProducts(int offset, int size){
        return this.findAll(Sort.descending("name")).page(Page.of(offset, size)).list();
    }

    public Optional<Product> getById(Long id){
        Product product = null;
        try {
            product = this.findById(id);
        } catch (NoResultException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(product);
    }

    public void persist(Product product) {
        // this.persist(product);
        PanacheRepository.super.persist(product);
    }

    public Optional<Product> update(Product product){
        final Long id = product.getId();
        Optional<Product> savedOpt = this.findByIdOptional(id);
        if (savedOpt.isEmpty()){
            return Optional.empty();
        }
        Product saved = savedOpt.get();
        saved.setName(product.getName());
        saved.setPrice(product.getPrice());
        saved.setDescribtion(product.getDescribtion());
        return Optional.of(saved);
    }
}
