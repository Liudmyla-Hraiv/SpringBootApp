package com.liutyk.first_demo.controllers;

import com.liutyk.first_demo.models.SessionSchedule;
import com.liutyk.first_demo.services.SessionScheduleServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/api/v1/session_schedule")
public class SessionScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(SessionScheduleController.class);
    private final SessionScheduleServer sessionScheduleServer;
    @Autowired
    public SessionScheduleController(SessionScheduleServer sessionScheduleServer) {
        this.sessionScheduleServer = sessionScheduleServer;
    }

    @GetMapping
    public ResponseEntity<?> getAllSessionSchedule() {
        try {
            List<SessionSchedule> list= sessionScheduleServer.getAllSessionSchedules();
            if (list.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schedules Not Found");
            }
            else return ResponseEntity.ok(list);
        }catch (Exception e){
            logger.error("ERROR 500: While get SessionSchedule list", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: GET ALL schedules: "+ e.getMessage());
        }
    }
    @RequestMapping("/")
    String home() {
        return "Welcome to SessionSchedule! Please delete '/' :)";
    }

    @GetMapping("/search/BySession")
    public ResponseEntity<?> getScheduleBySessionId(@RequestParam("id") Long id){
      try {
        List<SessionSchedule> schedules = sessionScheduleServer.getScheduleBySessionId(id);
        if (schedules.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schedule NOT FOUND by Session ID = " + id);
        }
        return ResponseEntity.ok(schedules);
    } catch (Exception e) {
          logger.error("ERROR 500: While get Schedule by Session", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: GET Schedule by Session ID: " + e.getMessage());
    }
    }
    @GetMapping("/search/ByRoom")
    public ResponseEntity<?> getByRoomIgnoreCase(@RequestParam("room") String room){
        try {
            List<SessionSchedule> schedules = sessionScheduleServer.getByRoomIgnoreCase(room);
            if (schedules.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schedule by Room NOT FOUND: " + room );
            }
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            logger.error("ERROR 500: While get Schedule by Room", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: GET schedule by ROOM Name: " + e.getMessage());
        }
    }
}
