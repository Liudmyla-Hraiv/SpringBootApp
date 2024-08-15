package com.liutyk.first_demo.REST_Assured;

import io.restassured.RestAssured;
import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
public class HomeAPITest {
    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port= 5000;
        RestAssured.basePath="/api/v1/";
    }
    @Test
    public void testGetVersion(){
        given()
                .when()
                .get("")
                .then()
                .statusCode(200)
                .body("version", equalTo("1.0.0"));
    }
}
