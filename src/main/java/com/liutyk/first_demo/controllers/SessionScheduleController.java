package com.liutyk.first_demo.controllers;

import com.liutyk.first_demo.models.SessionSchedule;
import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.repositories.SessionScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/session_schedule")
public class SessionScheduleController {
    @Autowired
    private SessionScheduleRepository sessionScheduleRepository;
    @GetMapping
    public ResponseEntity<?> getAllSessionSchedule() {
        try {
            List<SessionSchedule> list= sessionScheduleRepository.findAll();
            if (list.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Content");
            }
            else return ResponseEntity.ok(list);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR session_schedule GET: "+ e.getMessage());
        }
    }
    @RequestMapping("/")
    String home() {
        return "Welcome to SessionSchedule! Please delete '/' :)";
    }

    @GetMapping("/search/BySession")
    public ResponseEntity<?> findScheduleBySessionId(@RequestParam("id") Long id){
      try {
        List<SessionSchedule> schedules = sessionScheduleRepository.findScheduleBySessionId(id);
        if (schedules.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session Schedule  have not found by Session ID = " + id);
        }
        return ResponseEntity.ok(schedules);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR while search by Session ID " + e.getMessage());
    }
    }
    @GetMapping("/search/ByRoom")
    public ResponseEntity<?> findByRoomIgnoreCase(@RequestParam("room") String room){
        try {
            List<SessionSchedule> schedules = sessionScheduleRepository.findByRoomIgnoreCase(room);
            if (schedules.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session Schedule  have not found by Room : " + room );
            }
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR while search by Session ID " + e.getMessage());
        }
    }
}
