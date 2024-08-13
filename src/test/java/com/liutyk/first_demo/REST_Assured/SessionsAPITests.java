package com.liutyk.first_demo.REST_Assured;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //organizes the order of tests
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //organizes storage of test variables
public class SessionsAPITests {

    private   Integer testSessionId1;
    private  Integer testSessionId2;
    Integer randomSpeakerId=3;
    Integer randomSessionId=13;


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
                .statusCode(200)
                .body("size()",greaterThan(0) );

    }
    @Test
    @Order(2)
    public void testPostSession() {
        String reqBody = "{\n" +
                "\"sessionName\": \"Dicaprio's session\",\n" +
                "\"sessionDescription\": \"Dicaprio's's meeting with manager\",\n" +
                "\"sessionLength\": 30, \n" +
                "\"speakers\": [{\n"+
                            "\"speakerId\": " +randomSpeakerId + "\n"+
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
                    .body("speakers", hasItem(hasEntry("speakerId", randomSpeakerId)))
        .extract().response();
        testSessionId1 = response.path("sessionId");
        System.out.println("POST session 1 = " + testSessionId1);
    }
    @Test
    @Order(3)
    public void testPostSession2() {
        String reqBody = "{\n" +
                "\"sessionName\": \"Dicaprio's session\",\n" +
                "\"sessionDescription\": \"Dicaprio's's meeting with manager\",\n" +
                "\"sessionLength\": 30, \n" +
                "\"speakers\": [{\n"+
                "\"speakerId\": " +randomSpeakerId + "\n"+
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
                .body("speakers", hasItem(hasEntry("speakerId", randomSpeakerId)))
                .extract().response();
        testSessionId2 = response.path("sessionId");
        System.out.println("post session 2 = " + testSessionId2);
    }
    @Test
    @Order(4)
    public void testGetSessionByID(){
        given()
                .pathParam("id", randomSessionId)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("sessionId", notNullValue())
                .body("sessionId", equalTo(randomSessionId));
    }
    @Test
    @Order(5)
    public void testGetSessionByName() {
        List<String> nameParts = Arrays.asList("des", "DES", "spr", "SPR", "get", "gET");
        Random random = new Random();
        String randomNamePart = nameParts.get(random.nextInt(nameParts.size()));

        given()
                .queryParam("name", randomNamePart)
                .when()
                .get("/search/byName")
                .then()
                    .statusCode(200)
                    .body("sessionName", everyItem(containsStringIgnoringCase(randomNamePart)));
    }
    @Test
    @Order(6)
    public void testPutSession(){
        String reqBody="{\n" +
                "\"sessionName\": \"Dicaprio's session\", \n" +
                "\"sessionDescription\": \"Dicaprio meeting\", \n" +
                "\"sessionLength\": 30, \n" +
                " \"speakers\": []\n " +
                "}\n";
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", testSessionId1)
                .body(reqBody)
                .when()
                .put("/{id}")
                .then()
                    .statusCode(200)
                    .body("sessionName", equalTo("Dicaprio's session"))
                    .body("sessionDescription", equalTo("Dicaprio meeting"))
                    .body("speakers", hasItem(hasEntry("speakerId", randomSpeakerId)));
    }
    @Test
    @Order(7)
    public void testPatchSession(){
        String reqBody="{\n" +
                "\"sessionName\": \"Dicaprio's session\"\n" +
//                "\"sessionDescription\": \"Dicaprio meeting\", \n" +
//                "\"sessionLength\": 30, \n" +
//                " \"speakers\": []\n " +
                "}\n";
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", testSessionId1)
                .body(reqBody)
                .when()
                .patch("/{id}")
                .then()
                    .statusCode(200)
                    .body("sessionName", equalTo("Dicaprio's session"))
                    .body("sessionDescription", equalTo("Dicaprio meeting"))
                    .body("speakers", hasItem(hasEntry("speakerId", randomSpeakerId)));
    }
    @Test
    @Order(8)
    public void testDeleteSessions(){
        System.out.println("post session 2 deleteTest = " + testSessionId2);
        given()
                .pathParam("id", testSessionId2)
                .when()
                .delete("/{id}")
        .       then()
                    .statusCode(200)
                    .body(equalTo("Sessions "+ testSessionId2 + " and associated schedules deleted successfully"));
    }
    @Test
    @Order(9)
    public void testGetSessionsBySpeakerID(){
        given()
                .queryParam("id", randomSpeakerId)
                .when()
                .get("/search/bySpeaker")
                .then()
                    .statusCode(200)
                    .body("speakers", everyItem(hasItem(hasEntry("speakerId", randomSpeakerId))));
    }

}
