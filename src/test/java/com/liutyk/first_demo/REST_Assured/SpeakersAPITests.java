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
class SpeakersAPITests {
    Integer testSpeaker1=15;
    Integer testSpeaker2=1; //For Delete test

    Integer speakerId;
    Integer session_speakerId=53;//pair by speakerId

@BeforeAll
    public static void setup(){
    RestAssured.baseURI = "http://localhost";
    RestAssured.port= 5000;
    RestAssured.basePath="/api/v1/speakers";
    }

    @Test
    public void testGetAllSpeakers(){
    given()
            .when()
            .get("")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));

    }

    @Test
    public void testGetSpeakersByID(){
        given()
                .pathParam("id", testSpeaker1)
                .when()
                .get("/{id}")
                .then()
                    .statusCode(200)
                    .body("speakerId", notNullValue())
                    .body("speakerId", equalTo(testSpeaker1));
    }
    @Test
    public void testGetSpeakerByNameOrCompany() {
        List<String> nameParts = Arrays.asList("ser", "SER", "MAR", "mAR", "NIN", "nin");
        Random random = new Random();
        String randomNamePart = nameParts.get(random.nextInt(nameParts.size()));

        given()
                .queryParam("name", randomNamePart)
                .when()
                .get("/search/byName")
                .then()
                    .statusCode(200)
                    .body("$", not(empty()))
                    .body("findAll { it.firstName.toLowerCase().contains('" + randomNamePart.toLowerCase() + "') || it.lastName.toLowerCase().contains('" + randomNamePart.toLowerCase() + "') || it.company.toLowerCase().contains('" + randomNamePart.toLowerCase() + "') }", hasSize(greaterThan(0)));
        }

    @Test
    public void testGetSpeakerBySessionId(){

    Response response=
            given()
            .queryParam("id", session_speakerId)
            .when()
            .get("/search/bySession")
            .then()
            .statusCode(200)
            .body("$", not(empty()))
                    .extract().response();
        speakerId= response.path("get[0].speakerId");
    }
    @Test
    public void testPostSpeaker(){
        String reqBody=
                "{ \n"+
                        " \"firstName\": \"Sergio\", \n"+
                        "\"lastName\": \"Mario\",\n" +
                        "\"title\": \"Mario adventure\",\n" +
                        "\"company\": \"Nintendo\",\n" +
                        "\"speakerPhoto\": null,\n"+
                        "\"speakerBio\": \"The Nintendo Switch is a video game console developed by Nintendo\" \n"+
                        "}";
        given()
                .contentType(ContentType.JSON)
                .body(reqBody)
                .when()
                .post("/")
                .then()
                .statusCode(201)
                .body("speakerId", notNullValue())
                .body("firstName", equalTo("Sergio"))
                .body("lastName", equalTo("Mario"))
                .body("title", equalTo("Mario adventure"))
                .body("company", equalTo("Nintendo"))
                .body("speakerPhoto", equalTo(null))
                .body("speakerBio", equalTo("The Nintendo Switch is a video game console developed by Nintendo"));
    }
    @Test
    public void testPutSpeaker(){
        String reqBody=
                        "{ \n"+
                        " \"firstName\": \"Mario Bros\", \n"+
                        "\"lastName\": \"Brothers Mario\",\n" +
                        "\"title\": \"Mario adventure\",\n" +
                        "\"company\": \"Nintendo\",\n" +
                        "\"speakerPhoto\": null,\n"+
                        "\"speakerBio\": \"The Nintendo Switch is a video game console developed by Nintendo\" \n"+
                        "}";

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", testSpeaker1)
                .body(reqBody)
                .when()
                .put("/{id}")
                .then()
                    .statusCode(200)
                    .body("speakerId", equalTo(testSpeaker1))
                    .body("firstName", equalTo("Mario Bros"))
                    .body("lastName", equalTo("Brothers Mario"))
                    .body("title", equalTo("Mario adventure"))
                    .body("company", equalTo("Nintendo"))
                    .body("speakerPhoto", equalTo(null))
                    .body("speakerBio", equalTo("The Nintendo Switch is a video game console developed by Nintendo"));
    }
    @Test
    public void testPatchSpeaker(){
        String reqBody=
                 "{ \n"+
                 "\"title\": \"Mario NEW Adventures\"\n" +
                 "}";

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", testSpeaker1)
                .body(reqBody)
                .when()
                .patch("/{id}")
                .then()
                    .statusCode(200)
                    .body("speakerId", equalTo(testSpeaker1))
                    .body("title", equalTo("Mario NEW Adventures"));
    }
    @Test
    public void testDeleteSpeakers(){
        given()
                .pathParam("id", testSpeaker2)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(200)
                .body(equalTo("Speaker with ID = " + testSpeaker2 + " deleted"));
    }
}

