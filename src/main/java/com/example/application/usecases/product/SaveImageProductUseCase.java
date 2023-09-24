package com.example.application.usecases.product;

import java.io.File;
import java.util.List;

public interface SaveImageProductUseCase {
    List<String> saveImages(List<File> files);
}
