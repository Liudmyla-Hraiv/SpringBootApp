package com.liutyk.first_demo.controllers;

import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.services.SessionNotFoundException;
import com.liutyk.first_demo.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;


@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {
    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);
    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }


    @GetMapping
    public ResponseEntity<?> getAllSessions(){
        List<Session> allSessions = sessionService.getAllSessions();
        if(allSessions.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There ara NO any information about sessions");
        }
        else return ResponseEntity.ok(allSessions);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<?> getSessionById(@PathVariable Long id){
        try {
            Session getSession = sessionService.getSessionById(id);
            return ResponseEntity.ok(getSession);
        }catch(SessionNotFoundException e){
            logger.error("ERROR 404: While get Session by ID = "+ id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //SessionNotFoundException
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
            logger.error("ERROR 500: While get Session by Name = "+ name, e.getMessage(), e);
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
            logger.error("ERROR 500: While get Session by Speaker ID = " +id , e.getMessage(), e);
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
            logger.error("ERROR 500: While post Session", e.getMessage(), e);
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
        } catch (Exception e) {
            logger.error("ERROR 500: While put Session ID = "+id, e.getMessage(), e);
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
        } catch (Exception e) {
            logger.error("ERROR 500: While patch Session by ID = "+ id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: PATCH Session Request: " + e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSessionByID(@PathVariable Long id) {
        try {
            sessionService.deleteSessionById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Sessions " + id + " and associated schedules deleted successfully");
        } catch (SessionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("ERROR 500: While delete Session by ID = ", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: DELETE Session Request: " + e.getMessage());
        }
    }
}

