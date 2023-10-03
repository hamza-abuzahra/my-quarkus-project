package com.example.infrastructure.db;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.domain.IProductRepository;
import com.example.domain.Product;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class JpaProductRepository implements IProductRepository, PanacheRepository<JpaProduct> {

    @Override
    public List<Product> allProducts(int offset, int size) {
        List<JpaProduct> jpaProducts = findAll(Sort.ascending("name")).page(Page.of(offset, size)).list();
        List<Product> products = jpaProducts.stream().map((jpaProduct) -> {
            Product product = new Product(jpaProduct.getId(), jpaProduct.getName(), jpaProduct.getDescribtion(), jpaProduct.getPrice(), jpaProduct.getImageIds());
            return product;
        }).collect(Collectors.toList());
        return products;
    }
    
    @Override
    public int allProductsCount() {
        return findAll().list().size();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        try {
            JpaProduct jpaProduct = findById(id);
            if (jpaProduct != null){
                Product product = new Product(jpaProduct.getId(), jpaProduct.getName(), jpaProduct.getDescribtion(), jpaProduct.getPrice(), jpaProduct.getImageIds());
                return Optional.of(product);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> update(Product product) {
        // JpaProduct jpaProduct = mapDomainToJpa(product);
        final Long id = product.getId();
        if (id != null){
            JpaProduct saved = findById(id);
            if (saved != null){
                saved.setName(product.getName());
                saved.setPrice(product.getPrice());
                saved.setDescribtion(product.getDescribtion());
                saved.setImageIds(product.getImageIds());
                persist(saved);
                Product updatedProduct = new Product(saved.getId(), saved.getName(), saved.getDescribtion(), saved.getPrice(), saved.getImageIds());
                return Optional.of(updatedProduct);
            }
        }             
        return Optional.empty();
    }
    
    @Override
    @Transactional
    public void createProduct(Product product) {
        JpaProduct jpaProduct = new JpaProduct(product.getName(), product.getDescribtion(), product.getPrice(), product.getImageIds());
        persist(jpaProduct);
    }

    @Override
    public boolean deleteProductById(Long id) {
        return this.deleteById(id);
    }

    // for test purposes
    @Override
    public void deleteAllProducts() {
        deleteAll();
        String sql = "Select setval('jpaproduct_seq', 1)";
        list(sql);
    }    
}
