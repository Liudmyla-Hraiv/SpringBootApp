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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpeakersAPITests {
    private static Integer testSpeaker;
     Integer testSessionID=40;
@BeforeAll
    public static void setup(){
    RestAssured.baseURI = "http://localhost/api/v1/speakers";
    RestAssured.port= 5000;
    }

    @Test
    @Order(1)
    public void testGetSpeakersList(){
    given()
            .when()
            .get("")
            .then()
            .statusCode(200);

    }
    @Test
    @Order(2)
    public void testCreateSpeaker(){
        String reqBody=
                "{ \n"+
                        " \"firstName\": \"Sergio\", \n"+
                        "\"lastName\": \"Mario\",\n" +
                        "\"title\": \"Mario adventure\",\n" +
                        "\"company\": \"Nintendo\",\n" +
                        "\"speakerPhoto\": null,\n"+
                        "\"speakerBio\": \"The Nintendo Switch is a video game console developed by Nintendo\" \n"+
                        "}";
        Response response = given()
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
                .extract().response();
        testSpeaker = response.path("speakerId");
    }
    @Test
    @Order(3)
    public void testGetSpeakersByID(){
        given()
                .pathParam("id", testSpeaker)
                .when()
                .get("/{id}")
                .then()
                    .statusCode(200)
                    .body("speakerId", notNullValue())
                    .body("speakerId", equalTo(testSpeaker));
    }
    @Test
    @Order(4)
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
    @Order(5)
    public void testGetSpeakerBySessionId(){

    given()
            .queryParam("id", testSessionID)
            .when()
            .get("/search/bySession")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(6)
    public void testModifySpeaker(){
        String reqBody=
                        "{ \n"+
                        " \"firstName\": \"Mario Bros\", \n"+
                        "\"lastName\": \"Mario\",\n" +
                        "\"title\": \"Mario adventure 2\",\n" +
                        "\"company\": \"Nintendo\",\n" +
                        "\"speakerPhoto\": null,\n"+
                        "\"speakerBio\": \"The Nintendo Switch is a video game console developed by Nintendo\" \n"+
                        "}";

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", testSpeaker)
                .body(reqBody)
                .when()
                .put("/{id}")
                .then()
                    .statusCode(200)
                    .body("speakerId", equalTo(testSpeaker))
                    .body("firstName", equalTo("Mario Bros"))
                    .body("lastName", equalTo("Mario"))
                    .body("title", equalTo("Mario adventure 2"))
                    .body("company", equalTo("Nintendo"));
    }
    @Test
    @Order(7)
    public void testUpdateSpeaker(){
        String reqBody=
                 "{ \n"+
                 "\"title\": \"Mario NEW Adventures\"\n" +
                 "}";

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", testSpeaker)
                .body(reqBody)
                .when()
                .patch("/{id}")
                .then()
                    .statusCode(200)
                    .body("speakerId", equalTo(testSpeaker))
                    .body("title", equalTo("Mario NEW Adventures"));
    }
    @Test
    @Order(8)
    public void testDeleteSpeakers(){
        given()
                .pathParam("id", testSpeaker)
        .when()
                .delete("/{id}")
        .then()
                .statusCode(200)
                .body(equalTo("Speaker with ID = " + testSpeaker + " deleted"));
    }

}

