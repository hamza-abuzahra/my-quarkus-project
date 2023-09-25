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
import jakarta.transaction.Transactional;

@ApplicationScoped
public class JpaProductRepository implements IProductRepository, PanacheRepository<JpaProduct> {

    @Override
    public List<Product> allProducts(int offset, int size) {
        List<JpaProduct> jpaProducts = findAll(Sort.ascending("name")).page(Page.of(offset, size)).list();
        return mapJpaListToDomainList(jpaProducts);
    }
    
    @Override
    public int allProductsCount() {
        return findAll().list().size();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        Product product = null;
        try {
            JpaProduct jpaProduct = findById(id);
            if (jpaProduct != null){
                product = mapJpaToDomain(jpaProduct);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return Optional.ofNullable(product);
    }

    @Override
    public Optional<Product> update(Product product) {
        // JpaProduct jpaProduct = mapDomainToJpa(product);
        final Long id = product.getId();
        if (id == null){
            return Optional.empty();
        } 
        Optional<JpaProduct> savedOpt = findByIdOptional(id);
        if (savedOpt.isEmpty()){
            return Optional.empty();
        }
        JpaProduct saved = savedOpt.get();
        saved.setName(product.getName());
        saved.setPrice(product.getPrice());
        saved.setDescribtion(product.getDescribtion());
        saved.setImageIds(product.getImageIds());
        persist(saved);
        return Optional.of(mapJpaToDomain(saved));
    }
    
    @Override
    @Transactional
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
        product.setImageIds(jpaProduct.getImageIds());
        return product;
    }
    
    private static JpaProduct mapDomainToJpa(Product product){
        JpaProduct jpaProduct = new JpaProduct();
        jpaProduct.setName(product.getName());
        jpaProduct.setDescribtion(product.getDescribtion());
        jpaProduct.setPrice(product.getPrice());
        jpaProduct.setImageIds(product.getImageIds());
        return jpaProduct;
    }

    
}
