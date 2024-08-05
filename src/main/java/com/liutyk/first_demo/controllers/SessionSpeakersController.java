package com.liutyk.first_demo.controllers;

import com.liutyk.first_demo.models.SessionSpeakers;
import com.liutyk.first_demo.services.SessionSpeakersServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/session_speakers")
public class SessionSpeakersController {
    private final SessionSpeakersServer sessionSpeakersServer;
    @Autowired
    public SessionSpeakersController(SessionSpeakersServer sessionSpeakersServer) {
        this.sessionSpeakersServer = sessionSpeakersServer;
    }

    @GetMapping
    public ResponseEntity<?> list() {
        try {
            List<SessionSpeakers> list= sessionSpeakersServer.getAllSessionSpeakers();
            if (list.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Content");
            }
            else return  ResponseEntity.ok(list);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR SpeakerSession GET req: "+ e.getMessage());
        }
    }
    @GetMapping("/")
    String home() {
        return "Welcome to SpeakerSession! Please delete '/' :)";
    }

    @GetMapping("/search/BySession")
    public ResponseEntity<?> findBySessionId(@RequestParam("id") Long id){
        try {
            List<SessionSpeakers> sort = sessionSpeakersServer.findBySessionId(id);
            if (sort.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SessionSpeakers  have not found by Session ID = " + id);
            }
            return ResponseEntity.ok(sort);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR while search SessionSpeakers by Session ID " + e.getMessage());
        }
    }
    @GetMapping("/search/BySpeaker")
    public ResponseEntity<?> findBySpeakerId(@RequestParam("id") Long id){
        try {
            List<SessionSpeakers> sort = sessionSpeakersServer.findBySpeakerId(id);
            if (sort.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SessionSpeakers  have not found by Speaker ID = " + id);
            }
            return ResponseEntity.ok(sort);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR while search SessionSpeakers by Speaker ID " + e.getMessage());
        }
    }
}
