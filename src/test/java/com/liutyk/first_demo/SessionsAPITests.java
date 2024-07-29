package com.liutyk.first_demo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SessionsAPITests {

    private static Integer testSessionID;
    Integer testSpeakerID=1;
    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "http://localhost/api/v1/sessions";
        RestAssured.port= 5000;
    }
    @Test
    @Order(1)
    public void testGetSessionsList(){
        given()
                .when()
                    .get("")
                .then()
                    .statusCode(200);

    }
    @Test
    @Order(2)
    public void testCreateSession() {
        String reqBody = "{\n" +
                "\"sessionName\": \"Dicaprio's session\",\n" +
                "\"sessionDescription\": \"Dicaprio's's meeting with manager\",\n" +
                "\"sessionLength\": 30, \n" +
                "\"speakers\": [{\n"+
                            "\"speakerId\": " +testSpeakerID + "\n"+
                "}] \n"+
                "}\n";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(reqBody)
        .when()
                .post("/")
        .then()
                .statusCode(201)
                .body("sessionId", notNullValue())
                .body("sessionName", equalTo("Dicaprio's session"))
                .body("sessionDescription", equalTo("Dicaprio's's meeting with manager"))
                .body("sessionLength", equalTo(30))
                .body("speakers", hasItem(hasEntry("speakerId", testSpeakerID)))
        .extract().response();
        testSessionID = response.path("sessionId");
    }
    @Test
    @Order(3)
    public void testGetSessionByID(){
        given()
                .pathParam("id", testSessionID)
        .when()
                .get("/{id}")
        .then()
                .statusCode(200)
                .body("sessionId", notNullValue())
                .body("sessionId", equalTo(testSessionID));
    }
    @Test
    @Order(4)
    public void testGetSessionByName() {
        List<String> nameParts = Arrays.asList("dic", "DIC");
        Random random = new Random();
        String randomNamePart = nameParts.get(random.nextInt(nameParts.size()));

        given()
                .queryParam("name", randomNamePart)
        .when()
                .get("/search/ByName")
        .then()
                .statusCode(200)
                .body("sessionName", everyItem(containsStringIgnoringCase(randomNamePart)));
    }
    @Test
    @Order(5)
    public void testModifySession(){
        String reqBody="{\n" +
                "\"sessionName\": \"Dicaprio's session\", \n" +
                "\"sessionDescription\": \"Dicaprio meeting\", \n" +
                "\"sessionLength\": 30, \n" +
                " \"speakers\": []\n " +
                "}\n";
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", testSessionID)
                .body(reqBody)
        .when()
                .put("/{id}")
        .then()
                .statusCode(200)
                .body("sessionName", equalTo("Dicaprio's session"))
                .body("sessionDescription", equalTo("Dicaprio meeting"))
                .body("speakers", hasItem(hasEntry("speakerId", testSpeakerID)));
    }
    @Test
    @Order(6)
    public void testUpdateSession(){
        String reqBody="{\n" +
                "\"sessionName\": \"Dicaprio's session\", \n" +
                "\"sessionDescription\": \"Dicaprio meeting\", \n" +
                "\"sessionLength\": 30, \n" +
                " \"speakers\": []\n " +
                "}\n";
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", testSessionID)
                .body(reqBody)
        .when()
                .patch("/{id}")
        .then()
                .statusCode(200)
                .body("sessionName", equalTo("Dicaprio's session"))
                .body("sessionDescription", equalTo("Dicaprio meeting"))
                .body("speakers", hasItem(hasEntry("speakerId", testSpeakerID)));
    }
    @Test
    @Order(7)
    public void testDeleteSessions(){
        given()
                .pathParam("id", testSessionID)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(200)
                .body(equalTo("Sessions "+ testSessionID + " and associated schedules deleted successfully"));
    }
    @Test
    @Order(8)
    public void testGetSessionsBySpeakerID(){
        given()
                .queryParam("id", testSpeakerID)
        .when()
                .get("/search/BySpeaker")
        .then()
                .statusCode(200)
                .body("speakers", everyItem(hasItem(hasEntry("speakerId", testSpeakerID))));
    }

}
