package com.example.application.usecases.product;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import javax.imageio.ImageIO;


import com.example.domain.IProductRepository;
import com.example.domain.Product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class ProductService implements GetProductsUseCase, GetProductByIdUseCase, 
                CreateProductUseCase, UpdateProductUseCase, DeleteProductUseCase, 
                ProductCountUseCase, SaveImageProductUseCase, DeleteProductImagesUseCase {

    private final IProductRepository productRepo;
    private final String imagesFolderPath = "src/main/resources/META-INF/resources/";
    public ProductService(IProductRepository products){
        this.productRepo = products;
    }
    Logger logger = Logger.getLogger(ProductService.class.getName());

    @Override
    public List<Product> getProducts(int offset, int size) {
        List<Product> productsList = productRepo.allProducts(offset, size);
        return productsList;
    }    
    
    @Override
    public int productCount() {
        return productRepo.allProductsCount();
    }

    @Override
    public Optional<Product> getProductById(Long id) {      
        Optional<Product> resOptional = productRepo.getProductById(id);
        return resOptional;
    }

    @Override
    @Transactional
    public void createProduct(Product product){
        productRepo.createProduct(product);
    }

    @Override
    @Transactional
    public Optional<Product> updateProduct(Long id, Product product){
        Optional<Product> oldProduct = productRepo.getProductById(id);
        if (!oldProduct.isEmpty()){
            deleteLinkedImages(oldProduct.get());
            product.setId(id);
            Optional<Product> res = productRepo.update(product);
            return res;
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public boolean deleteProduct(Long id){
        Product deleteProduct = productRepo.getProductById(id).get();
        if (productRepo.deleteProductById(id)) {
            if (deleteLinkedImages(deleteProduct)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> saveImages(List<File> files) {
        List<String> imageIds = new ArrayList<>();
        try {
            for (File file : files) {
                String imageId = UUID.randomUUID().toString();
                BufferedImage image = ImageIO.read(file);
                File imageFile = new File(imagesFolderPath + "images/" + imageId + ".jpg");
                ImageIO.write(image, "jpg", imageFile); 
                imageIds.add("/images/"+imageId+".jpg");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }  
        return imageIds;
    }

    @Override
    public boolean deleteLinkedImages(Product product) {
        try {
            for (String filePath : product.getImageIds()) {
                Path imagePath = Paths.get(imagesFolderPath + filePath);
                try {
                    Files.deleteIfExists(imagePath);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

