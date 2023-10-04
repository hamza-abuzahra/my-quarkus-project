package com.example.presenter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.example.domain.IUserRepository;
import com.example.domain.User;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
@ActivateRequestContext
public class UserResourceTest {

    Logger logger = Logger.getLogger(UserResourceTest.class.getName());

    @Inject
    UserResource userResource;

    @Inject
    IUserRepository userRepository;
    
    @BeforeAll
    @Transactional
    void setup() {
        User first = new User(1L, "apple", "a green apple", "good@exampl.e");        
        User second = new User(2L, "table", "a white table", "good@exapl.e");
        User third = new User(3L, "book", "a black book", "good@expl.e");
        User fourth = new User(4L, "cave", "a very cold and dark cave", "good@examp.e");

        userRepository.createUser(first);        
        userRepository.createUser(second);
        userRepository.createUser(third);
        userRepository.createUser(fourth);
    }

    @Test
    public void testGetProductByIdEndpointInvalid(){
        
        given()
          .when().get("/api/users/9")
          .then()
            .statusCode(404).body(
                "message", is("User not found")
            );
    }
    @Test
    public void testGetUserByIdEndpoint(){
        given()
        .when().get("/api/users/14")
        .then()
        .statusCode(200).body("fname", is("cave"));
    }
    // @Test 
    // public void testCreateUserEndpointOk(){
    //     String accessToken = given().baseUri("http://localhost:9090")
    //             .contentType(ContentType.URLENC)
    //             .formParam("grant_type", "password")
    //             .formParam("client_id", "test")
    //             .formParam("client_secret", "OeXgcXGjXZ3bmwsI9bcjLgb7KrxEi43P")
    //             .formParam("username", "admin")
    //             .formParam("password", "admin")
    //             .post("/realms/test/protocol/openid-connect/token")
    //             .jsonPath().getString("access_token");
    //     given().auth().oauth2(accessToken)
    //     .contentType(ContentType.JSON)
    //     .body("{\"user\":{\"fname\":\"testuser15\",\"lname\":\"test\",\"email\":\"ts@xampl5.com\"},\"roles\": [\"user\"]}")
    //     .when()
    //     .post("api/users")
    //     .then()
    //     .statusCode(200);
    // }
    @Test 
    public void testCreateUserEndpointNullName(){
        String accessToken = given().baseUri("http://localhost:9090")
                .contentType(ContentType.URLENC)
                .formParam("grant_type", "password")
                .formParam("client_id", "test")
                .formParam("client_secret", "OeXgcXGjXZ3bmwsI9bcjLgb7KrxEi43P")
                .formParam("username", "admin")
                .formParam("password", "admin")
                .post("/realms/test/protocol/openid-connect/token")
                .jsonPath().getString("access_token");
        given().auth().oauth2(accessToken)
        .contentType(ContentType.JSON)
        .body("{\"user\":{\"id\":\"1\",\"fname\":\"\",\"lname\":\"test\",\"email\":\"ts@xampl5.com\"},\"roles\": [\"user\"]}")
        .when()
        .post("api/users")
        .then()
        .statusCode(400)
        .contentType(ContentType.JSON)
        .body("violations[0].message", 
        is("User name can not be blank"));
    }

    @Test 
    public void testCreateUserInvalidEmail(){
        String accessToken = given().baseUri("http://localhost:9090")
                .contentType(ContentType.URLENC)
                .formParam("grant_type", "password")
                .formParam("client_id", "test")
                .formParam("client_secret", "OeXgcXGjXZ3bmwsI9bcjLgb7KrxEi43P")
                .formParam("username", "admin")
                .formParam("password", "admin")
                .post("/realms/test/protocol/openid-connect/token")
                .jsonPath().getString("access_token");
        given().auth().oauth2(accessToken)
        .contentType(ContentType.JSON)
        .body("{\"user\":{\"id\":\"1\",\"fname\":\"sdf\",\"lname\":\"test\",\"email\":\"exampleemai.l\"},\"roles\": [\"user\"]}")
        .when()
        .post("api/users")
        .then()
        .statusCode(400)
        .contentType(ContentType.JSON)
        .body("violations[0].message", 
        is("email not valid"));
    }
}