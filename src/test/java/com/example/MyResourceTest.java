package com.example;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
@QuarkusTest
public class MyResourceTest {

    @Test
    public void testGetAllProductsEndpoint() {
        given().when().get("/api/products").then().statusCode(200).body(
            "$.size()", is(4), 
            "[0].name", is("table"));
    }

    @Test
    public void testGetProductByIdEndpoint(){
        given()
          .when().get("/api/products/1")
          .then()
            .statusCode(200).body(
                "name", is("apple"),
                "price", is(12.0f)
            );
    }
    @Test
    public void testGetProductByIdEndpointInvalid(){
        given()
        .when().get("/api/products/5")
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

        given()
        .when()
        .get("api/products/5").then().statusCode(200).body("name", is("test"));
    }
    @Test
    public void testUpdateProduct(){
        given()
        .body(new Product("banana", "a very ripe and yellow one", 23))
        .contentType(ContentType.JSON)
        .when()
        .put("api/products/1")
        .then()
        .statusCode(200)
        .body("id", is(1));
        
        given()
        .when()
        .get("api/products/1")
        .then()
        .statusCode(200)
        .body("name", is("banana"));

        // non exisiting product
        given()
        .body(new Product("banan", "a very ripe and yellow one", 23))
        .contentType(ContentType.JSON)
        .when()
        .put("api/products/7")
        .then()
        .statusCode(400);
    }
    @Test
    public void testDeleteProduct(){
        given()
        .when()
        .delete("api/products/2")
        .then()
        .statusCode(200);

        given()
        .when()
        .get("api/products/2")
        .then()
        .statusCode(400);

        // invalid 
         given()
        .when()
        .delete("api/products/2")
        .then()
        .statusCode(304);
    }

}