package com.example.application.usecases.product;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.example.domain.IProductRepository;
import com.example.domain.ImageSaveService;
import com.example.domain.Product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class ProductService implements GetProductsUseCase, GetProductByIdUseCase, 
                CreateProductUseCase, UpdateProductUseCase, DeleteProductUseCase, 
                ProductCountUseCase, SaveImageProductUseCase {

    private final IProductRepository productRepo;
    private final ImageSaveService imageSaveService;
    public final String imagesFolderPath = "src/main/resources/META-INF/resources/";
    public ProductService(IProductRepository products, ImageSaveService imageSaveService){
        this.productRepo = products;
        this.imageSaveService = imageSaveService;
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
            imageSaveService.deleteLinkedImages(oldProduct.get(), imagesFolderPath);
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
        try {
            Optional<Product> deleteProduct = productRepo.getProductById(id);
            if (!deleteProduct.isEmpty()) {
                logger.info("im here because");
                if (imageSaveService.deleteLinkedImages(deleteProduct.get(), imagesFolderPath)) {
                    productRepo.deleteProductById(id);
                    return true;
                }
            } 
            logger.info("wooww ");
        } catch (Exception e) {
            logger.info("im here because" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> saveImages(List<File> files) {
        return imageSaveService.saveImages(files, imagesFolderPath);
    }

    // @Override
    // public boolean deleteLinkedImages(Product product) {
    //     try {
    //         for (String filePath : product.getImageIds()) {
    //             Path imagePath = Paths.get(imagesFolderPath + filePath);
    //             try {
    //                 Files.deleteIfExists(imagePath);
    //             } catch (IOException e){
    //                 e.printStackTrace();
    //             }
    //         }
    //         return true;
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return false;
    // }
}

