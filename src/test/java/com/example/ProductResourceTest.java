package com.example;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

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
          .when().get("/api/products/7")
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
        .statusCode(400);
    }
    @Test 
    public void testCreateProductEndpoint(){
        given()
        .body(new Product("test", "good thing", 3))
        .contentType(ContentType.JSON)
        .when()
        .post("api/products")
        .then()
        .statusCode(200);

    }
    @Test
    public void testUpdateProduct(){
        given()
        .body(new Product("banana", "a very ripe and yellow one", 23))
        .contentType(ContentType.JSON)
        .when()
        .put("api/products/7")
        .then()
        .statusCode(200)
        .body("id", is(7));
        
        given()
        .when()
        .get("api/products/7")
        .then()
        .statusCode(200)
        .body("name", is("banana"));

        // non exisiting product
        given()
        .body(new Product("banan", "a very ripe and yellow one", 23))
        .contentType(ContentType.JSON)
        .when()
        .put("api/products/10000")
        .then()
        .statusCode(400);
    }
    @Test
    public void testDeleteProduct(){
        given()
        .when()
        .delete("api/products/7")
        .then()
        .statusCode(200);

        // invalid 
        given()
        .when()
        .delete("api/products/2")
        .then()
        .statusCode(304);
    }

}