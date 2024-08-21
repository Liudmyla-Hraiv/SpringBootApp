package com.liutyk.first_demo.controllers;

import com.liutyk.first_demo.models.SessionSpeaker;
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
public class SessionSpeakerController {
    private final SessionSpeakersServer sessionSpeakersServer;
    @Autowired
    public SessionSpeakerController(SessionSpeakersServer sessionSpeakersServer) {
        this.sessionSpeakersServer = sessionSpeakersServer;
    }

    @GetMapping
    public ResponseEntity<?> getAllSessionSpeakers() {
        try {
            List<SessionSpeaker> list= sessionSpeakersServer.getAllSessionSpeakers();
            if (list.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Data");
            }
            else return  ResponseEntity.ok(list);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: SpeakerSession GET ALL: "+ e.getMessage());
        }
    }
    @GetMapping("/")
    String home() {
        return "Welcome to SpeakerSession! Please delete '/' :)";
    }

    @GetMapping("/search/BySession")
    public ResponseEntity<?> getBySessionId(@RequestParam("id") Long id){
        try {
            List<SessionSpeaker> sort = sessionSpeakersServer.getBySessionId(id);
            if (sort.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SessionSpeakers  don't found by Session ID = " + id);
            }
            return ResponseEntity.ok(sort);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR while search SessionSpeakers by Session ID: " + e.getMessage());
        }
    }
    @GetMapping("/search/BySpeaker")
    public ResponseEntity<?> getBySpeakerId(@RequestParam("id") Long id){
        try {
            List<SessionSpeaker> sort = sessionSpeakersServer.getBySpeakerId(id);
            if (sort.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SessionSpeakers  don't found by Speaker ID = " + id);
            }
            return ResponseEntity.ok(sort);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR while search SessionSpeakers by Speaker ID: " + e.getMessage());
        }
    }
}
