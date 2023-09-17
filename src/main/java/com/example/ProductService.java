package com.example;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import javax.naming.directory.InvalidAttributesException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProductService implements IProductService {

    private final IProductRepository productRepo;
    // PanacheRepository<Product>

    public ProductService(IProductRepository products){
        this.productRepo = products;
    }

    @Override
    public List<Product> getProducts(int offset, int size) throws InvalidParameterException{
        List<Product> productsList = this.productRepo.allProducts(offset, size);
        if (productsList.size() == 0){
            throw new InvalidParameterException("No products in database");
        } else{
            return productsList;
        }
    }    

    @Override
    public Optional<Product> getProductById(Long id) throws InvalidParameterException{
        Optional<Product> resOptional = this.productRepo.getById(id);
        if (resOptional.isEmpty()){
            throw new InvalidParameterException("No products found");    
        }
        return resOptional;
    }

    @Override
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
        this.productRepo.createProduct(product);
    }

    @Override
    @Transactional
    public Product udpateProduct(Long id, Product product) throws InvalidParameterException{
        product.setId(id);
        Optional<Product> res = this.productRepo.update(product);
        if (res.isEmpty()){
            // LOGGER.info("res is null");
            throw new InvalidParameterException("Product not found");
        } else {
            return res.get();
        }
    }

    @Override
    @Transactional
    public boolean deleteProduct(Long id){
        return this.productRepo.deleteProductById(id);
    }
}

