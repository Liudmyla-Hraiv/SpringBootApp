package com.liutyk.first_demo.controllers;

import com.liutyk.first_demo.models.SessionSpeaker;
import com.liutyk.first_demo.services.SessionSpeakersServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/v1/session_speakers")
public class SessionSpeakerController {
    private static final Logger logger = LoggerFactory.getLogger(SessionSpeakerController.class);
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
            logger.error("ERROR 500: While get Session - Speaker list {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: SpeakerSession GET ALL: Server error");
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
            logger.error("ERROR 500: While get Speakers by Session ID = {} : {}",id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR while search SessionSpeakers by Session ID: Server error");
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
            logger.error("ERROR 500: While get Sessions by Speaker ID = {} : {}" ,id , e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR while search SessionSpeakers by Speaker ID: Server error");
        }
    }
}
