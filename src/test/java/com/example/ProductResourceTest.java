package com.example;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
@QuarkusTest
@TestInstance(value = Lifecycle.PER_CLASS)
public class ProductResourceTest {

    @Inject
    ProductResource productResource;

    Logger logger = Logger.getLogger(ProductRepositoryTest.class.getName());
    @BeforeAll
    @Transactional
    void setup() {
        Product first = new Product("apple", "a green apple", 12);        
        productResource.createProduct(first);
        Product second = new Product("table", "a white table", 450.2f);
        productResource.createProduct(second);
        Product third = new Product("book", "a black book", 5.5f);
        Product fourth = new Product("cave", "a very cold and dark cave", 19899.99f);
        productResource.createProduct(third);        
        productResource.createProduct(fourth);
    }

    @Test
    public void testGetAllProductsEndpoint() {
        given().when().get("/api/products").then().statusCode(200).body(
            "$.size()", is(4), 
            "[0].name", is("table"));
    }

    @Test
    public void testGetProductByIdEndpoint(){
        given()
          .when().get("/api/products/9")
          .then()
            .statusCode(200).body(
                "name", is("table"),
                "price", is(450.2f)
            );
    }
    @Test
    public void testGetProductByIdEndpointInvalid(){
        given()
        .when().get("/api/products/10000")
        .then()
        .statusCode(404);
    }
    @Test 
    public void testCreateProductEndpointOk(){
        given()
        .body("{\"name\":\"hi\", \"desc\":\"whatever\", \"price\":\"23\"}")
        .contentType(ContentType.JSON)
        .when()
        .post("api/products")
        .then()
        .statusCode(200);
    }
    @Test 
    public void testCreateProductEndpointNotOk(){
        given()
        .body("{\"id\":\"10\",\"name\":\"hi\", \"desc\":\"whatever\", \"price\":\"23\"}")
        .contentType(ContentType.JSON)
        .when()
        .post("api/products")
        .then()
        .statusCode(400);
    }
    @Test 
    public void testCreateProductEndpointNameNotProvided(){
        given()
        .body("{\"desc\":\"whatever\", \"price\":\"23\"}")
        .contentType(ContentType.JSON)
        .when()
        .post("api/products")
        .then()
        .statusCode(400);
    }
    @Test 
    public void testCreateProductEndpointPriceIsZero(){
        given()
        .body("{\"name\":\"hi\",\"desc\":\"whatever\", \"price\":\"0\"}")
        .contentType(ContentType.JSON)
        .when()
        .post("api/products")
        .then()
        .statusCode(400);
    }
    @Test
    public void testUpdateProduct(){
        given()
        .contentType(ContentType.JSON)
        .body("{\"name\":\"hi\", \"desc\":\"whatever\", \"price\":\"23\"}")
        .when()
        .put("api/products/10")
        .then()
        .statusCode(200);
    }
    @Test
    public void testUpdateProductNameValidation(){
        given()
        .contentType(ContentType.JSON)
        .body("{\"desc\":\"whatever\", \"price\":\"23\"}")
        .when()
        .put("api/products/10")
        .then()
        .statusCode(400);
    }
    @Test
    public void testUpdateProductPriceValidation(){
        given()
        .contentType(ContentType.JSON)
        .body("{\"name\":\"hi\",\"desc\":\"whatever\", \"price\":\"0\"}")
        .when()
        .put("api/products/10")
        .then()
        .statusCode(400);
    }
    @Test
    public void testUpdateProductIdValidation(){
        given()
        .contentType(ContentType.JSON)
        .body("{\"id\":\"2\",\"name\":\"hi\",\"desc\":\"whatever\", \"price\":\"0\"}")
        .when()
        .put("api/products/10")
        .then()
        .statusCode(400);
    }
    @Test
    public void testUpdateProductNotExists(){
        // non exisiting product
        given()
        .contentType(ContentType.JSON)
        .body("{\"name\":\"bye\", \"desc\":\"whatever\", \"price\":\"23\"}")
        .when()
        .put("api/products/10000")
        .then()
        .statusCode(404);
    }
    @Test
    public void testDeleteProduct(){
        given()
        .when()
        .delete("api/products/9")
        .then()
        .statusCode(200);

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