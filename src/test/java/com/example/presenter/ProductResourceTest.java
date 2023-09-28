package com.example.presenter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.example.domain.IProductRepository;
import com.example.domain.Product;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.transaction.annotations.Rollback;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@Rollback(true)
public class ProductResourceTest {

    @Inject
    ProductResource productResource;

    @Inject 
    IProductRepository productRepository;

    Logger logger = Logger.getLogger(ProductResourceTest.class.getName());
    @BeforeAll
    @Transactional
    void setup() {
        productRepository.deleteAllProducts();
        Product first = new Product("apple", "a green aple", 12);        
        productResource.createProduct(first, new ArrayList<File>());
        Product second = new Product("table", "a white table", 450.2f);
        productResource.createProduct(second, new ArrayList<File>());
        Product third = new Product("book", "a black book", 5.5f);
        productResource.createProduct(third, new ArrayList<File>());        
        Product fourth = new Product("cave", "a very cold and dark cave", 19899.99f);
        productResource.createProduct(fourth, new ArrayList<File>());
    }
 
    @AfterAll
    @Transactional
    void teraDown() {
        // productRepository.deleteAllProducts();
    }
    @Test
    public void testGetAllProductsEndpoint() {
        given().when().get("/api/products").then().statusCode(200).body(
            "products.size()", is(4), 
            "products[0].name", is("apple"));
    } 

    @Test
    public void testGetProductByIdEndpointInvalid(){
        given()
          .when().get("/api/products/9")
          .then()
            .statusCode(404).body(
                "message", is("Product not found")
            );
    }
    @Test
    public void testGetProductByIdEndpoint(){
        given()
        .when().get("/api/products/19")
        .then()
        .statusCode(200).body("name", is("apple"));
    }
    @Test 
    public void testCreateProductEndpointOk(){
        given()
        .multiPart("product", "{\"id\":\"10\",\"name\":\"hi\", \"describtion\":\"whatever\", \"price\":\"23\"}", "application/json")
        .multiPart("image", new ArrayList<File>())
        .when()
        .post("api/products")
        .then()
        .statusCode(200);
    }
    @Test 
    public void testCreateProductEndpointNotOk(){
        given()
        .multiPart("product", "{\"id\":\"10\",\"name\":\"\", \"describtion\":\"whatever\", \"price\":\"23\"}", "application/json")
        .multiPart("image", new ArrayList<File>())
        .when()
        .post("api/products")
        .then()
        .statusCode(400)
        .contentType(ContentType.JSON)
        .body("violations[0].message", 
        is("Product name cannot be blank"));
    }

    @Test 
    public void testCreateProductEndpointPriceIsZero(){
        given()
        .multiPart("product", "{\"name\":\"hi\",\"describtion\":\"whatever\", \"price\":\"0\"}", "application/json")
        .multiPart("image", new ArrayList<File>())
        .when()
        .post("api/products")
        .then()
        .statusCode(400)
        .contentType(ContentType.JSON)
        .body("violations[0].message", 
        is("Product price must be positive"));

    }
    @Test
    public void testUpdateProduct(){
        // File file = new File("src/main/resources/META-INF/resources/images/26a891c5-2c82-433a-8e14-c92b0d724720.jpg");
        // File file2 = new File("src/main/resources/META-INF/resources/images/26a891c5-2c82-433a-8e14-c92b0d724720.jpg"); 
        // logger.info(file.getName() + " hiii");
        // List<File> files = new ArrayList<>();
        // files.add(file);
        // files.add(file2);
        given()
        .multiPart("product", "{\"name\":\"updated\",\"describtion\":\"new desc\", \"price\":\"15\"}", "application/json")
        // .multiPart("image", files.get(0), "image/jpg")
        // .multiPart("image", files.get(1), "image/jpg")
        .when()
        .put("api/products/21")
        .then()
        .statusCode(200)
        .body("name", is("updated"),
            "describtion", is("new desc"),
            "price", is(15f));
            // "imageIds.size()", is(files.size()));
    }

    @Test
    public void testUpdateProductNameValidation(){
        given()
        .multiPart("product", "{\"describtion\":\"whatever\", \"price\":\"23\"}", "application/json")
        .when()
        .put("api/products/2")
        .then()
        .statusCode(400)
        .contentType(ContentType.JSON)
        .body("violations[0].message", 
        is("Product name cannot be blank"));
    }
    @Test
    public void testUpdateProductPriceValidation(){
        given()
        .multiPart("product", "{\"name\":\"updated\", \"describtion\":\"whatever\", \"price\":\"0\"}", "application/json")
        .when()
        .put("api/products/10")
        .then()
        .statusCode(400)
        .contentType(ContentType.JSON)
        .body("violations[0].message", 
        is("Product price must be positive"));
    }
    @Test
    public void testUpdateProductNotExists(){
        // non exisiting product
        given()
        .multiPart("product", "{\"name\":\"bye\", \"describtion\":\"whatever\", \"price\":\"23\"}", "application/json")        
        .when()
        .put("api/products/10000")
        .then()
        .statusCode(404);
    }
    @Test
    public void testDeleteProduct(){
        given()
        .when()
        .delete("api/products/20")
        .then()
        .statusCode(200).body("message", is("product with the id 20 deleted successfully"));
    }

    @Test
    public void testDeleteProductNotExists(){
        // invalid 
        given()
        .when()
        .delete("api/products/2")
        .then()
        .statusCode(404);
    }
}