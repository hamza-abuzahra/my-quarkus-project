// package com.example.storage;

// import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
// import static org.junit.jupiter.api.Assertions.assertFalse;

// import java.io.File;
// import java.util.ArrayList;
// import java.util.List;

// import org.junit.jupiter.api.Test;

// import com.example.domain.Product;

// import io.quarkus.test.junit.QuarkusTest;
// import jakarta.inject.Inject;

// @QuarkusTest
// public class OnPremiseStorageServiceTest {
    
//     @Inject
//     private OnPremiseImageStorage onPremiseImageStorage;
//     private String path = "src/test/java/com/example/storage/";

//     @Test
//     public void testSavingImages(){
//         File file = new File("src/main/resources/META-INF/resources/images/1a2e9574-d58a-4d07-9374-8a2953966c55.jpg");
//         File file2 = new File("src/main/resources/META-INF/resources/images/5b46fe93-c47b-4f5a-843e-47664ae86a02.jpg");
//         List<File> files = new ArrayList<File>();
//         files.add(file);
//         files.add(file2);
//         List<String> imagePaths = onPremiseImageStorage.saveImages(files, path);
//         assertDoesNotThrow(() -> new File(path+imagePaths.get(0)));
//     }

//     @Test
//     public void testDeleteLinkedImages() {
//         Product product1 = new Product("apple", "red apple", 23f);
//         product1.setImageIds(List.of("/images/f80c5394-deec-4e12-9657-628bbf172cc8.jpg", "/images/8b10ae04-a514-4fb1-8272-ed2255149e37.jpg"));
//         onPremiseImageStorage.deleteLinkedImages(product1, path);
//         File file = new File(path+"/images" ,"1a74bc02-aec9-4654-b4c7-220455d7456e.jpg");
//         assertFalse(file.exists());
//     }
// }
