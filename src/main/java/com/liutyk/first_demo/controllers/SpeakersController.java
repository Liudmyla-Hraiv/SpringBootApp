package com.liutyk.first_demo.controllers;


import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.repositories.SessionRepository;
import com.liutyk.first_demo.repositories.SpeakerRepository;
import com.liutyk.first_demo.repositories.SessionSpeakersRepository;
import com.liutyk.first_demo.services.SpeakerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/speakers")
public class SpeakersController {
    private final SpeakerService speakerService;

    @Autowired
    public SpeakersController(SpeakerService speakerService) {
        this.speakerService = speakerService;
    }

    //GET all speakers
    @GetMapping
    public ResponseEntity<?> getAllSpeakers() {
        List<Speaker> speakers = speakerService.getAllSpeakers();
        if(speakers.isEmpty()){
            return status(HttpStatus.NOT_FOUND).body("No speakers found");
        } else return ResponseEntity.ok(speakers);
    }
//GET speaker with speakerID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSpeakerById(@PathVariable Long id) {
        Speaker speaker = speakerService.getSpeakerById(id).orElse(null);
        if (speaker==null){
            return status(HttpStatus.NOT_FOUND).body("Speaker with ID = "+ id + "  have not found ");
        }else  return ResponseEntity.ok(speaker);
    }
//GET by part of First/Last or company name with ignore CASE
    @GetMapping("/search/byName")
    public ResponseEntity<?> findByKeywordIgnoreCase(@RequestParam String name) {
        try {
            List<Speaker> speakers = speakerService.findByKeywordIgnoreCase(name);
            if (speakers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Speakers  have not found with keyword: " + name);
            }
            return ResponseEntity.ok(speakers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR while search by FirstName OR LastName OR Company");
        }
    }
//GET speaker by sessionId
    @GetMapping("/search/bySession")
    public ResponseEntity<?> findSpeakerBySessionId(@RequestParam Long id) {
        try {
            List<Speaker> speakers = speakerService.findBySessionId(id);
            if (speakers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Speakers  have not found by Session ID: " + id);
            }
            return ResponseEntity.ok(speakers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR while search by Session ID ");
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createSpeaker(@RequestBody Speaker speaker) {
        try {
            Speaker savedSpeaker = speakerService.createSpeaker(speaker);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSpeaker);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                    .body("Error occurred while creating speaker");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatesSpeaker(@PathVariable Long id, @RequestBody Speaker speaker){
        try {
            Speaker updatedSpeaker = speakerService.updateSpeaker(id, speaker);
            return ResponseEntity.ok(updatedSpeaker);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: PATCH Speaker " + e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> modifySpeaker(@PathVariable Long id, @RequestBody Speaker speaker){
        try {
            Speaker modifiedSpeaker = speakerService.modifySpeaker(id, speaker);
            return ResponseEntity.ok(modifiedSpeaker);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: PUT Speaker request " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpeaker(@PathVariable Long id) {
        try {
            speakerService.deleteSpeaker(id);
            return ResponseEntity.ok("Speaker with ID = " + id + " deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR while DELETE speaker");
        }
    }


}