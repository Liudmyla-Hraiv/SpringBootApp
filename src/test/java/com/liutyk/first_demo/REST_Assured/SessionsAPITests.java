package com.liutyk.first_demo.REST_Assured;

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
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //organizes storage of test variables
public class SessionsAPITests {

    Integer testSessionId1= 75;
    Integer testSessionId2=67;
    Integer randomSpeakerId=3;
    Integer randomSessionId=13;


    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port= 5000;
        RestAssured.basePath="/api/v1/sessions";
    }

    @Test
    public void testGetAllSessions(){
        given()
                .when()
                .get("")
                .then()
                .statusCode(200)
                .body("size()",greaterThan(0) );

    }
    @Test
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
