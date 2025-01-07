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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SessionsAPITests {

    Integer testSpeakerId = 20;
    Integer testSessionId1 =22; //for Delete test
    Integer testSessionId2 =25;
    Integer session2_SpeakerId; // pair from testSessionId


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
    public void testGetSessionByID(){
        Response response =
                given()
                .pathParam("id", testSessionId2)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("sessionId", notNullValue())
                .body("sessionId", equalTo(testSessionId2))
        .extract().response();
        session2_SpeakerId = response.path("speakers[0].speakerId");
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
    public void testGetSessionsBySpeakerID(){
        given()
                .queryParam("id", testSpeakerId)
                .when()
                .get("/search/bySpeaker")
                .then()
                .statusCode(200)
                .body("speakers", everyItem(hasItem(hasEntry("speakerId", testSpeakerId))));
    }
    @Test
    public void testPostSession() {
        String reqBody = "{\n" +
                "\"sessionName\": \"Dicaprio's session\",\n" +
                "\"sessionDescription\": \"Dicaprio's's meeting with manager\",\n" +
                "\"sessionLength\": 30, \n" +
                "\"speakers\": [{\n"+
                "\"speakerId\": " + testSpeakerId + "\n"+
                "}] \n"+
                "}\n";

        given()
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
                .body("speakers", hasItem(hasEntry("speakerId", testSpeakerId)));
    }
    @Test
    public void testPutSession(){
        String reqBody = "{\n" +
                "\"sessionName\": \"Dicaprio's session\",\n" +
                "\"sessionDescription\": \"Dicaprio meeting\",\n" +
                "\"sessionLength\": 30, \n" +
                "\"speakers\": [{\n"+
                "\"speakerId\": " + session2_SpeakerId + "\n"+
                "}] \n"+
                "}\n";


        given()
                .contentType(ContentType.JSON)
                .pathParam("id", testSessionId2)
                .body(reqBody)
                .when()
                .put("/{id}")
                .then()
                    .statusCode(200)
                    .body("sessionId", equalTo(testSessionId2))
                    .body("sessionName", equalTo("Dicaprio's session"))
                    .body("sessionDescription", equalTo("Dicaprio meeting"))
                    .body("sessionLength", equalTo(30))
                    .body("speakers", hasItem(hasEntry("speakerId", session2_SpeakerId)));
    }
    @Test
    public void testPatchSession(){
        String reqBody="{\n" +
                "\"sessionName\": \"Updated Session\",\n" +
                "\"sessionDescription\": \"Updated Description\", \n" +
                "\"sessionLength\": 30 \n" +
//                " \"speakers\": []\n " +
                "}\n";
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", testSessionId2)
                .body(reqBody)
                .when()
                .patch("/{id}")
                .then()
                .statusCode(200)
                .body("sessionId", equalTo(testSessionId2))
                .body("sessionName", equalTo("Updated Session"))
                .body("sessionDescription",equalTo("Updated Description") )
                .body("sessionLength", equalTo(30));
    }
    @Test
    public void testDeleteSessions(){
        given()
                .pathParam("id", testSessionId1)
                .when()
                .delete("/{id}")
                .then()
                    .statusCode(200)
                    .body(equalTo("Sessions "+ testSessionId1 + " and associated schedules deleted successfully"));
    }
}
