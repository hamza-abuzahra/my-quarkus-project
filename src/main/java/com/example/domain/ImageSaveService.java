package com.example.domain;

import java.io.File;
import java.util.List;

public interface ImageSaveService {
    List<String> saveImages(List<File> files, String imageFolderPath);
    boolean deleteLinkedImages(Product product, String imageFolderPath);
}
