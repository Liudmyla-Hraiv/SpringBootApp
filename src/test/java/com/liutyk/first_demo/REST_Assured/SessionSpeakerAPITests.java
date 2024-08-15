package com.liutyk.first_demo.REST_Assured;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class SessionSpeakerAPITests {
    Integer SpeakID=25;
    Integer SesID=25;
    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port= 5000;
        RestAssured.basePath="/api/v1/session_speakers";
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

        given()
                .queryParam("id", SesID)
                .when()
                .get("/search/BySession")
                .then()
                    .statusCode(200)
                    .body("[0].session.sessionId", equalTo(SesID));
    }
    @Test
    @Order(3)
    public void testGetBySpeakerId() {

        given()
                .queryParam("id", SpeakID)
                .when()
                .get("/search/BySpeaker")
                .then()
                    .statusCode(200)
                    .body("[0].session.speakers.find { it.speakerId == " + SpeakID + " }.speakerId", equalTo(SpeakID));
    }
}
