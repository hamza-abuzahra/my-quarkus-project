package com.example;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import javax.naming.directory.InvalidAttributesException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class ProductService {

    private final ProductRepository products;
    
    public List<Product> getProducts(int offset, int size){
        return products.allProducts(offset, size);
    }    

    public Optional<Product> getProductById(Long id){
       return products.getById(id);
    }

    @Transactional
    public void newProduct(Product product) throws InvalidAttributesException{
        if (product.getId() != null){
            throw new InvalidAttributesException("Id must not be filled");
        } 
        if (product.getName() == null){
            throw new InvalidAttributesException("Name must be filled");

        }
        if (product.getPrice() == 0.0f){
            throw new InvalidAttributesException("Price must be filled");

        }
        products.persist(product);
    }

    @Transactional
    public Product udpate(Long id, Product product) throws InvalidParameterException{
        product.setId(id);
        Optional<Product> res = products.update(product);
        if (res.isEmpty()){
            // LOGGER.info("res is null");
            throw new InvalidParameterException("Product not found");
        } else {
            return res.get();
        }
    }

    @Transactional
    public boolean deleteProduct(Long id){
        return products.deleteById(id);
    }
}

