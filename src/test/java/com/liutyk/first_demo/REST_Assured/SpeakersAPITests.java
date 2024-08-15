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
import static io.restassured.RestAssured.sessionId;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //organizes the order of tests
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //organizes storage of test variables
class SpeakersAPITests {
    private  Integer testSpeaker1;
    private Integer testSpeaker2;
    Integer randomSessionId = 33;
    Integer randomSpeakerId=27;

@BeforeAll
    public static void setup(){
    RestAssured.baseURI = "http://localhost";
    RestAssured.port= 5000;
    RestAssured.basePath="/api/v1/speakers";
    }

    @Test
    @Order(1)
    public void testGetSpeakersList(){
    given()
            .when()
            .get("")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));

    }
    @Test
    @Order(2)
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
        testSpeaker1 = response.path("speakerId");
        System.out.println("POST sp1: "+ testSpeaker1);
    }
    @Test
    @Order(3)
    public void testPostSpeaker2(){
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
        testSpeaker2 = response.path("speakerId");
        System.out.println("POST sp2: "+ testSpeaker2);

    }
    @Test
    @Order(4)
    public void testGetSpeakersByID(){
        given()
                .pathParam("id", randomSpeakerId)
                .when()
                .get("/{id}")
                .then()
                    .statusCode(200)
                    .body("speakerId", notNullValue())
                    .body("speakerId", equalTo(randomSpeakerId));
    }
    @Test
    @Order(5)
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
    @Order(6)
    public void testGetSpeakerBySessionId(){

    given()
            .queryParam("id", randomSessionId)
            .when()
            .get("/search/bySession")
            .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    @Order(7)
    public void testPutSpeaker(){
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
                .pathParam("id", testSpeaker1)
                .body(reqBody)
                .when()
                .put("/{id}")
                .then()
                    .statusCode(200)
                    .body("speakerId", equalTo(testSpeaker1))
                    .body("firstName", equalTo("Mario Bros"))
                    .body("lastName", equalTo("Mario"))
                    .body("title", equalTo("Mario adventure 2"))
                    .body("company", equalTo("Nintendo"));
    }
    @Test
    @Order(8)
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
    @Order(9)
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

