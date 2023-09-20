package com.example.db;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.domain.IProductRepository;
import com.example.domain.Product;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JpaProductRepository implements IProductRepository, PanacheRepository<JpaProduct> {

    @Override
    public List<Product> allProducts(int offset, int size) {
        List<JpaProduct> jpaProducts = findAll(Sort.descending("name")).page(Page.of(offset, size)).list();
        return mapJpaListToDomainList(jpaProducts);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        Product product = null;
        try {
            product = mapJpaToDomain(findById(id));
        } catch (Exception e){
            e.printStackTrace();
        }
        return Optional.ofNullable(product);
    }

    @Override
    public Optional<Product> update(Product product) {
        JpaProduct jpaProduct = mapDomainToJpa(product);
        final Long id = jpaProduct.getId();
    
        Optional<JpaProduct> savedOpt = findByIdOptional(id);
        if (savedOpt.isEmpty()){
            return Optional.of(mapJpaToDomain(savedOpt.get()));
        }
        JpaProduct saved = savedOpt.get();
        saved.setName(product.getName());
        saved.setPrice(product.getPrice());
        saved.setDescribtion(product.getDescribtion());
        persist(saved);
        return Optional.of(mapJpaToDomain(saved));
    }

    @Override
    public void createProduct(Product product) {
        persist(mapDomainToJpa(product));
    }

    @Override
    public boolean deleteProductById(Long id) {
        return this.deleteById(id);
    }

    @Override
    public void deleteAllProducts() {
        deleteAll();
    }

    private List<Product> mapJpaListToDomainList(List<JpaProduct> jpaProducts) {
        return jpaProducts.stream()
            .map(JpaProductRepository::mapJpaToDomain)
            .collect(Collectors.toList());
    }

    private static Product mapJpaToDomain(JpaProduct jpaProduct){
        Product product = new Product();
        product.setId(jpaProduct.getId());
        product.setName(jpaProduct.getName());
        product.setDescribtion(jpaProduct.getDescribtion());
        product.setPrice(jpaProduct.getPrice());
        return product;
    }
    
    private static JpaProduct mapDomainToJpa(Product product){
        JpaProduct jpaProduct = new JpaProduct();
        jpaProduct.setName(jpaProduct.getName());
        jpaProduct.setDescribtion(jpaProduct.getDescribtion());
        jpaProduct.setPrice(jpaProduct.getPrice());
        return jpaProduct;
    }
}
