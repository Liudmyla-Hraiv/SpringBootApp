package com.liutyk.first_demo.controllers;


import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.repositories.SessionRepository;
import com.liutyk.first_demo.repositories.SpeakerRepository;
import com.liutyk.first_demo.repositories.SessionSpeakersRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1/speakers")
public class SpeakersController {
    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionSpeakersRepository sessionSpeakersRepository;
//GET all speakers
    @GetMapping
    public ResponseEntity<?> getAllSpeakers() {
        List<Speaker> speakers = speakerRepository.findAll();
        if(speakers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No speakers found");
        } else return ResponseEntity.ok(speakers);
    }
//GET speaker with speakerID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSpeakerById(@PathVariable Long id) {
        Speaker speaker = speakerRepository.findById(id).orElse(null);
        if (speaker==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Speaker with ID = "+ id + "  have not found ");
        }else  return ResponseEntity.ok(speaker);
    }
//GET by part of First/Last or company name with ignore CASE
    @GetMapping("/search/ByName")
    public ResponseEntity<?> findByKeywordIgnoreCase(@RequestParam String name) {
        try {
            List<Speaker> speakers = speakerRepository.findByKeywordIgnoreCase(name);
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
    @GetMapping("/search/BySession")
    public ResponseEntity<?> findSpeakerBySessionId(@RequestParam Long id) {
        try {
            List<Speaker> speakers = speakerRepository.findBySessionId(id);
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
            Speaker savedSpeaker =speakerRepository.saveAndFlush(speaker);

            return ResponseEntity.status(201).body(savedSpeaker);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Error occurred while creating speaker ");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatesSpeaker(@PathVariable Long id, @RequestBody Speaker speaker){
        try{
            Optional<Speaker> optional = speakerRepository.findById(id);
            if (optional.isEmpty()){
                return status(HttpStatus.NOT_FOUND).body("Speaker with ID " + id + " not found");
            }
            Speaker existingSpeaker= optional.get();
            if (speaker.getFirstName() != null && !speaker.getFirstName().isEmpty()) {
                existingSpeaker.setFirstName(speaker.getFirstName());}
            if (speaker.getLastName() != null && !speaker.getLastName().isEmpty()) {
                existingSpeaker.setLastName(speaker.getLastName());}
            if (speaker.getTitle() != null && !speaker.getTitle().isEmpty()) {
                existingSpeaker.setTitle(speaker.getTitle());}
            if (speaker.getCompany() != null && !speaker.getCompany().isEmpty()) {
                existingSpeaker.setCompany(speaker.getCompany());}
            if (speaker.getSpeakerBio()!= null && !speaker.getSpeakerBio().isEmpty()) {
                existingSpeaker.setSpeakerBio(speaker.getSpeakerBio());}

            Speaker sp = speakerRepository.saveAndFlush(existingSpeaker);
            return ResponseEntity.ok(sp);
        }catch (Exception e){
            e.printStackTrace();
            return status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: PATCH Session " +e.getMessage() );
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> modifySpeaker(@PathVariable Long id, @RequestBody Speaker speaker){

        try {
            Optional<Speaker> optionalSpeaker = speakerRepository.findById(id);
            if (optionalSpeaker.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Speaker with ID = " + id + " is not found");
            } else{
                Speaker existingSpeaker=optionalSpeaker.get();
                //"speaker_id" is a primary key, and we don't want to replace it.
                BeanUtils.copyProperties(speaker, existingSpeaker, "speakerId");
                Speaker sp = speakerRepository.saveAndFlush(existingSpeaker);
                return ResponseEntity.ok(sp);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: PUT Speaker request" +e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpeaker(@PathVariable Long id) {
        try{
            if(!speakerRepository.existsById(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Speaker with ID = "+id+" does not exist");
            }
            speakerRepository.deleteById(id);
            return ResponseEntity.ok("Speaker with ID = "+ id+ " deleted");
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR while DELETE speaker");
        }

    }



}