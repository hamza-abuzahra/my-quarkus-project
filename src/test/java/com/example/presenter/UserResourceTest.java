// package com.example.presenter;

// import static io.restassured.RestAssured.given;
// import static org.hamcrest.CoreMatchers.is;

// import java.util.logging.Logger;

// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import org.junit.jupiter.api.TestInstance.Lifecycle;

// import com.example.domain.User;

// import io.quarkus.test.junit.QuarkusTest;
// import io.restassured.http.ContentType;
// import jakarta.inject.Inject;
// import jakarta.transaction.Transactional;

// @QuarkusTest
// @TestInstance(Lifecycle.PER_CLASS)
// public class UserResourceTest {

//     Logger logger = Logger.getLogger(UserResourceTest.class.getName());
//     @Inject
//     UserResource userResource;

//     @BeforeAll
//     @Transactional
//     void setup() {
//         User first = new User(1L, "apple", "a green apple", "good@exampl.e");        
//         User second = new User(2L, "table", "a white table", "good@exapl.e");
//         User third = new User(3L, "book", "a black book", "good@expl.e");
//         User fourth = new User(4L, "cave", "a very cold and dark cave", "good@examp.e");
//         userResource.createUser(first);
//         userResource.createUser(second);
//         userResource.createUser(third);        
//         userResource.createUser(fourth);
//     }

//     @Test
//     public void testGetProductByIdEndpointInvalid(){
//         given()
//           .when().get("/api/users/9")
//           .then()
//             .statusCode(404).body(
//                 "message", is("User not found")
//             );
//     }
//     @Test
//     public void testGetUserByIdEndpoint(){
//         given()
//         .when().get("/api/users/13")
//         .then()
//         .statusCode(200).body("fname", is("cave"));
//     }
//     @Test 
//     public void testCreateUserEndpointOk(){
//         given()
//         .contentType(ContentType.JSON)
//         .body("{\"id\":\"10\", \"fname\":\"hi\", \"lname\":\"whatever\", \"email\":\"example@emai.l\"}")
//         .when()
//         .post("api/users")
//         .then()
//         .statusCode(200);
//     }
//     @Test 
//     public void testCreateUserEndpointNullName(){
//         given()
//         .contentType(ContentType.JSON)
//         .body("{\"id\":\"10\", \"fname\":\"\",\"lname\":\"whatever\", \"email\":\"example@emai.l\"}")
//         .when()
//         .post("api/users")
//         .then()
//         .statusCode(400)
//         .contentType(ContentType.JSON)
//         .body("violations[0].message", 
//         is("User name can not be blank"));
//     }

//     @Test 
//     public void testCreateProductEndpointPriceIsZero(){
//         given()
//         .contentType(ContentType.JSON)
//         .body("{\"id\":\"10\", \"fname\":\"hi\",\"lname\":\"whatever\", \"email\":\"exampleemai.l\"}")
//         .when()
//         .post("api/users")
//         .then()
//         .statusCode(400)
//         .contentType(ContentType.JSON)
//         .body("violations[0].message", 
//         is("email not valid"));
//     }
// }