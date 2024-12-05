package com.liutyk.first_demo.controllers;


import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.services.SpeakerNotFoundException;
import com.liutyk.first_demo.services.SpeakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/speakers")
public class SpeakerController {
    private static final Logger logger = LoggerFactory.getLogger(SpeakerController.class);
    private final SpeakerService speakerService;

    @Autowired
    public SpeakerController(SpeakerService speakerService) {
        this.speakerService = speakerService;
    }

    //GET all speakers
    @GetMapping
    public ResponseEntity<?> getAllSpeakers() {
        List<Speaker> speakers = speakerService.getAllSpeakers();
        if(speakers.isEmpty()){
            return status(HttpStatus.NOT_FOUND).body("Speakers don't found");
        } else return ResponseEntity.ok(speakers);
    }
//GET speaker with speakerID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSpeakerById(@PathVariable Long id) {
        try {
            Speaker speaker = speakerService.getSpeakerById(id);
            return ResponseEntity.ok(speaker);
        }catch (SpeakerNotFoundException e) {
            logger.error("ERROR 404: While get Speaker by ID = "+ id, e.getMessage(), e);
            return status(HttpStatus.NOT_FOUND).body(e.getMessage()); //SpeakerNotFoundException
        }
    }
//GET by part of First/Last or company name with ignore CASE
    @GetMapping("/search/byName")
    public ResponseEntity<?> getSpeakerByKeywordIgnoreCase(@RequestParam String name) {
        try {
            List<Speaker> speakers = speakerService.getSpeakerByKeywordIgnoreCase(name);
            if (speakers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Speakers don't found with keyword: " + name);
            }
            return ResponseEntity.ok(speakers);
        } catch (Exception e) {
            logger.error("ERROR 500: While get Speaker by Keyword = " + name, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: in getByKeywordIgnoreCase");
        }
    }
//GET speaker by sessionId
    @GetMapping("/search/bySession")
    public ResponseEntity<?> getSpeakerBySessionId(@RequestParam Long id) {
        try {
            List<Speaker> speakers = speakerService.getSpeakerBySessionId(id);
            if (speakers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Speaker don't found by Session ID: " + id);
            }
            return ResponseEntity.ok(speakers);
        } catch (Exception e) {
            logger.error("ERROR 500: While get Speaker by Session ID = "+ id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: in getSpeakerBySessionId");
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> postSpeaker(@RequestBody Speaker speaker) {
        try {
            Speaker savedSpeaker = speakerService.postSpeaker(speaker);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSpeaker);
        } catch (Exception e) {
            logger.error("ERROR 400: While post Speaker", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("ERROR: POST speaker: FirstName OR LastName Or Title OR Company Or speakerBio");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchSpeaker(@PathVariable Long id, @RequestBody Speaker speaker){
        try {
            Speaker updatedSpeaker = speakerService.patchSpeaker(id, speaker);
            return ResponseEntity.ok(updatedSpeaker);
        } catch (SpeakerNotFoundException e) {
            logger.error("ERROR 404: While patch Speaker by ID = " + id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //SpeakerNotFoundException
        } catch (Exception e) {
            logger.error("ERROR 500: While patch Speaker by ID = "+id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: PATCH Speaker: " + e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> putSpeaker(@PathVariable Long id, @RequestBody Speaker speaker){
        try {
            Speaker modifiedSpeaker = speakerService.putSpeaker(id, speaker);
            return ResponseEntity.ok(modifiedSpeaker);
        } catch (SpeakerNotFoundException e) {
            logger.error("ERROR 404: While put Speaker by ID = "+ id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());//SpeakerNotFoundException
        } catch (Exception e) {
            logger.error("ERROR 500: While put Speaker by ID = "+id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: PUT Speaker: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpeaker(@PathVariable Long id) {
        try {
            speakerService.deleteSpeaker(id);
            return ResponseEntity.ok("Speaker with ID = " + id + " deleted");
        } catch (SpeakerNotFoundException e) {
            logger.error("ERROR 404: While delete Speaker by ID = "+id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());//SpeakerNotFoundException
        } catch (Exception e) {
            logger.error("ERROR 500: While delete Speaker by ID = "+ id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR while DELETE speaker");
        }
    }


}