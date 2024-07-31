package com.liutyk.first_demo;

import com.liutyk.first_demo.models.Session;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class SessionScheduleAPITests {

    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "http://localhost/api/v1/session_schedule";
        RestAssured.port= 5000;
    }

    @Test
    @Order(1)
    public void testGetScheduleList(){
        given()
                .when()
                .get("")
                .then()
                    .statusCode(200);

    }
    @Test
    @Order(2)
    public void testGetScheduleByRoom() {
        List<String> roomPart = Arrays.asList("hic", "HIC");
        Random random = new Random();
        String randomRoomPart = roomPart.get(random.nextInt(roomPart.size()));

        given()
                .queryParam("room", randomRoomPart)
                .when()
                .get("/search/ByRoom")
                .then()
                    .statusCode(200)
                    .body("room", everyItem(containsStringIgnoringCase(randomRoomPart)));
    }
    @Test
    @Order(3)
    public void testGetScheduleBySessionId() {
        Integer SesID= 35;

        given()
                .queryParam("id", SesID)
                .when()
                .get("/search/BySession")
                .then()
                    .statusCode(200)
                    .body("[0].sessionId.sessionId", equalTo(SesID));
    }

}
