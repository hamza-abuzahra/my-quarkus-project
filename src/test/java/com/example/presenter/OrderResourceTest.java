// package com.example.presenter;

// import static io.restassured.RestAssured.given;
// import static org.hamcrest.CoreMatchers.is;

// import java.io.File;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.logging.Logger;

// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import org.junit.jupiter.api.TestInstance.Lifecycle;

// import com.example.domain.Order;
// import com.example.domain.Product;
// import com.example.domain.User;

// import io.quarkus.test.junit.QuarkusTest;
// import io.quarkus.transaction.annotations.Rollback;
// import io.restassured.http.ContentType;
// import jakarta.inject.Inject;
// import jakarta.transaction.Transactional;

// @QuarkusTest
// @TestInstance(Lifecycle.PER_CLASS)
// @Transactional
// @Rollback(true)
// public class OrderResourceTest {
    
//     @Inject
//     private OrderResource orderResource;
//     @Inject
//     private UserResource userResource;
//     @Inject 
//     private ProductResource productResource;

//     Logger logger = Logger.getLogger(OrderResourceTest.class.getName());

//     @BeforeAll
//     @Transactional
//     void setup() {
//         User user = new User(10L, "apple", "a yellow apple", "good@exapl.e"); 
//         userResource.createUser(user);
//         Product product = new Product(18L,"apple", "a red apple", 12);        
//         productResource.createProduct(product, new ArrayList<File>());
//         Order order = new Order(user, List.of(product));
//         orderResource.createOrder(order);
//     }
//     @Test
//     public void testGetOrders() {
//         given()
//         .when().get("/api/orders")
//         .then()
//         .statusCode(200)
//         .body("orders.size()", is(1), "orders[0].user.id", is(10), "numOfPages", is(1));
//     }

//     @Test
//     public void testGetOrderByIdOk() {
//         given()
//         .when().get("/api/orders/9")
//         .then()
//         .statusCode(200).body("user.id", is(10));
//     }

//     @Test 
//     public void testGetOrderByIdNotOk() {
//         given()
//         .when().get("/api/orders/7")
//         .then()
//         .statusCode(404)
//         .body("message", is("Order not found"));
//     }

//     @Test 
//     @Transactional
//     public void testCreateOrderValid() {
//         given()
//         .contentType(ContentType.JSON)
//         .body("{\"user\":{\"id\":\"10\", \"fname\":\"apple\", \"lname\":\"a yellow apple\", \"email\":\"good@exapl.e\"}, \"products\":[{\"id\":\"18\",\"name\":\"apple\",\"describtion\":\"a red apple\", \"price\":\"12\"}]}")
//         .when().post("/api/orders")
//         .then()
//         .statusCode(200)
//         .body(is("order added successfully"));
//     }

//     @Test
//     public void testCreateOrderNotValid() {
//         given()
//         .contentType(ContentType.JSON)
//         .body("{\"user\":{\"id\":\"1\", \"fname\":\"apple\", \"lname\":\"a yellow apple\", \"email\":\"good@exapl.e\"}, \"products\":[{\"id\":\"18\",\"name\":\"apple\",\"describtion\":\"a red apple\", \"price\":\"12\"}]}")
//         .when().post("/api/orders")
//         .then()
//         .statusCode(400)
//         .body("violations", is("one of the given fields do not exist, either user or one of the products"));
//     }

//     @Test
//     public void testCreateOrderValidationMissingUser() {
//         given()
//         .contentType(ContentType.JSON)
//         .body("{\"products\":[{\"id\":\"18\",\"name\":\"apple\",\"describtion\":\"a red apple\", \"price\":\"12\"}]}")
//         .when().post("/api/orders")
//         .then()
//         .statusCode(400)
//         .body("violations[0].message", 
//         is("user id must be provided"));
//     }

//     @Test
//     public void testCreateOrderNotValidMissingProducts() {
//         given()
//         .contentType(ContentType.JSON)
//         .body("{\"user\":{\"id\":\"1\", \"fname\":\"apple\", \"lname\":\"a yellow apple\", \"email\":\"good@exapl.e\"}}")
//         .when().post("/api/orders")
//         .then()
//         .statusCode(400)
//         .body("violations[0].message", is("products cannot be empty"));
//     }

//     @Test
//     public void testCreateOrderNotValidEmptyProducts() {
//         given()
//         .contentType(ContentType.JSON)
//         .body("{\"user\":{\"id\":\"1\", \"fname\":\"apple\", \"lname\":\"a yellow apple\", \"email\":\"good@exapl.e\"}, \"products\":[]}")
//         .when().post("/api/orders")
//         .then()
//         .statusCode(400)
//         .body("violations[0].message", is("products cannot be empty"));
//     }
// }
