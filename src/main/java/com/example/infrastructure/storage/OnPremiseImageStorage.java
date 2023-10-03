package com.example.infrastructure.storage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.example.domain.ImageSaveService;
import com.example.domain.Product;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OnPremiseImageStorage implements ImageSaveService {

    @Override
    public List<String> saveImages(List<File> files, String imagesFolderPath) {
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
    public boolean deleteLinkedImages(Product product, String imagesFolderPath) {
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
