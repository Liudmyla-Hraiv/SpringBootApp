package com.liutyk.first_demo.controllers;

import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.services.SessionNotFoundException;
import com.liutyk.first_demo.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/sessions")
public class SessionsController {

    private final SessionService sessionService;

    @Autowired
    public SessionsController(SessionService sessionService) {
        this.sessionService = sessionService;
    }


    @GetMapping
    public ResponseEntity<?> getAllSessions(){
        List<Session> allSessions = sessionService.getAllSessions();
        if(allSessions.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There ara NO any information about session");
        }
        else return ResponseEntity.ok(allSessions);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<?> getSessionById(@PathVariable Long id){
       Optional<Session> getSession = sessionService.getSessionById(id);
        if (getSession.isPresent()) {
            return ResponseEntity.ok(getSession);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session Not found");
        }
    }

    @GetMapping("/search/byName")
    public ResponseEntity<?> getSessionByPartialName(@RequestParam String name) {
        try {
            List<Session> sessions = sessionService.getSessionByPartialName(name);
            if (sessions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session with name " + name + " not found" );
            } else {
                return ResponseEntity.ok(sessions);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: GET Session by Name Request: " + e.getMessage());
        }
    }
    @GetMapping("/search/bySpeaker")
    public ResponseEntity<?> getSessionsBySpeakerId(@RequestParam Long id) {
        try {
            List<Session> sessions = sessionService.getSessionsBySpeakerId(id);
            if (sessions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No sessions found for speaker with ID: " + id);
            }
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: GET by SpeakerId: " + e.getMessage());
        }
    }
    @PostMapping("/")
    public ResponseEntity<?> postSession(@RequestBody Session session ){
        try {
            if (session.getSpeakers()!=null && !session.getSpeakers().isEmpty()) {
                Session ses = sessionService.postSession(session);
                return ResponseEntity.status(HttpStatus.CREATED).body(ses);
            }else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Session must have at least one speaker");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: POST Session Request: " + e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> putSession (@PathVariable Long id, @RequestBody Session session){
        try {
            Session ses = sessionService.putSession(id, session);
            return ResponseEntity.status(HttpStatus.OK).body(ses);
        } catch (SessionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: PUT Session Request: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchSession(@PathVariable Long id, @RequestBody Session session){
        try {
            Session ses = sessionService.patchSession(id, session);
            return ResponseEntity.status(HttpStatus.OK).body(ses);
        } catch (SessionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: PATCH Session Request: " + e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteByID(@PathVariable Long id) {
        try {
            sessionService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Sessions " + id + " and associated schedules deleted successfully");
        } catch (SessionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: DELETE Session Request: " + e.getMessage());
        }
    }
}

