package com.liutyk.first_demo.controllers;

import com.liutyk.first_demo.models.Session;
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
    public ResponseEntity<?> list(){
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }
    }

    @GetMapping("/search/ByName")
    public ResponseEntity<?> getSessionByPartialName(@RequestParam String name) {
        try {
            List<Session> sessions = sessionService.getSessionByPartialName(name);
            if (!sessions.isEmpty()) {
                return ResponseEntity.ok(sessions);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session with name " + name + " not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: GET Session by Name Request: " + e.getMessage());
        }
    }
    @GetMapping("/search/BySpeaker")
    public ResponseEntity<?> getSessionsBySpeakerId(@RequestParam Long id) {
        try {
            List<Session> sessions = sessionService.getSessionsBySpeakerId(id);
            if (sessions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No sessions found for speaker with ID: " + id);
            }
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("ERROR: GET by SpeakerId" + e.getMessage());
        }
    }
    @PostMapping("/")
    public ResponseEntity<?> createSession(@RequestBody Session session ){
        try {
            Session ses = sessionService.createSession(session);
            return ResponseEntity.status(HttpStatus.CREATED).body(ses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: POST Session Request " + e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSession (@PathVariable Long id, @RequestBody Session session){
        try {
            Session ses = sessionService.updateSession(id, session);
            return ResponseEntity.status(HttpStatus.OK).body(ses);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: PUT Session Request" + e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> modifySession(@PathVariable Long id, @RequestBody Session session){
        try {
            Session ses = sessionService.modifySession(id, session);
            return ResponseEntity.status(HttpStatus.OK).body(ses);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: PATCH Session Request " + e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteByID(@PathVariable Long id) {
        try {
            sessionService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Sessions " + id + " and associated schedules deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: DELETE Session Request " + e.getMessage());
        }
    }
}

