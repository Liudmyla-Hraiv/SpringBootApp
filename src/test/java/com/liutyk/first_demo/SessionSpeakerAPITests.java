package com.liutyk.first_demo;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class SessionSpeakerAPITests {

    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "http://localhost/api/v1/session_speakers";
        RestAssured.port= 5000;
    }

    @Test
    @Order(1)
    public void testGetSession_SpeakerList(){
        given()
                .when()
                .get("")
                .then()
                    .statusCode(200);

    }
    @Test
    @Order(2)
    public void testGetBySessionId() {
        Integer SesID=25;
        given()
                .queryParam("id", SesID)
                .when()
                .get("/search/BySession")
                .then()
                    .statusCode(200)
                    .body("[0].sessionId.sessionId", equalTo(SesID));
    }
    @Test
    @Order(3)
    public void testGetBySpeakerId() {
        Integer SpeakID=25;
        given()
                .queryParam("id", SpeakID)
                .when()
                .get("/search/BySpeaker")
                .then()
                    .statusCode(200)
                    .body("[0].sessionId.speakers.find { it.speakerId == " + SpeakID + " }.speakerId", equalTo(SpeakID));
    }
}
