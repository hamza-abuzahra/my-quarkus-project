package com.example.presenter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.example.domain.Order;
import com.example.domain.Product;
import com.example.domain.User;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
public class OrderResourceTest {
    
    @Inject
    private OrderResource orderResource;
    @Inject
    private UserResource userResource;
    @Inject 
    private ProductResource productResource;

    @BeforeAll
    @Transactional
    void setup() {
        User user = new User(1L, "apple", "a green apple", "good@exampl.e");        
        userResource.createUser(user);
        Product product = new Product("apple", "a green apple", 12);        
        productResource.createProduct(product, new ArrayList<File>());
        
        Order order = new Order(1L, List.of(1L));
        orderResource.createOrder(order);
    }
    
    @Test
    public void testGetOrders() {
        given()
        .when().get("/api/orders")
        .then()
        .statusCode(200)
        .body("$.size()", is(1), "[0].userId", is(1));
    }

    @Test
    public void testGetOrderByIdOk() {
        given()
        .when().get("/api/orders/1")
        .then()
        .statusCode(200).body("userId", is(1));
    }

    @Test 
    public void testGetOrderByIdNotOk() {
        given()
        .when().get("/api/orders/7")
        .then()
        .statusCode(404)
        .body("message", is("Order not found"));
    }

    @Test 
    public void testCreateOrderValid() {
        given()
        .contentType(ContentType.JSON)
        .body("{\"userId\":\"1\", \"productId\":[\"1\"]}")
        .when().post("/api/orders")
        .then()
        .statusCode(200)
        .body(is("order added successfully"));
    }

    @Test
    public void testCreateOrderNotValid() {
        given()
        .contentType(ContentType.JSON)
        .body("{\"userId\":\"2\", \"productId\":[\"1\"]}")
        .when().post("/api/orders")
        .then()
        .statusCode(400)
        .body("violations", is("one of the given fields do not exist, either user or one of the products"));
    }

    @Test
    public void testCreateOrderValidationMissingUserId() {
        given()
        .contentType(ContentType.JSON)
        .body("{\"productId\":[\"1\"]}")
        .when().post("/api/orders")
        .then()
        .statusCode(400)
        .body("violations[0].message", 
        is("user id must be provided"));
    }

    @Test
    public void testCreateOrderNotValidMissingProducts() {
        given()
        .contentType(ContentType.JSON)
        .body("{\"userId\":\"2\"}")
        .when().post("/api/orders")
        .then()
        .statusCode(400)
        .body("violations[0].message", is("products cannot be empty"));
    }

    @Test
    public void testCreateOrderNotValidEmptyProducts() {
        given()
        .contentType(ContentType.JSON)
        .body("{\"userId\":\"2\", \"productId\":[]}")
        .when().post("/api/orders")
        .then()
        .statusCode(400)
        .body("violations[0].message", is("products cannot be empty"));
    }
}
