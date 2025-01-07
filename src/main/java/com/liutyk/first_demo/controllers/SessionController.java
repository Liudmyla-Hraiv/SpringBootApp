package com.liutyk.first_demo.controllers;

import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.services.SessionNotFoundException;
import com.liutyk.first_demo.services.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController extends BaseController {
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
            logger.error("ERROR 404: While get ALL sessions");
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
            logger.error("ERROR 404: While get Session by ID = {} : SessionNotFoundException - {}",  id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search/byName")
    public ResponseEntity<?> getSessionByPartialName(@RequestParam String name) {
        try {
            List<Session> sessions = sessionService.getSessionByPartialName(name);
            if (sessions.isEmpty()) {
                logger.error("ERROR 404: While get session by name =  {}",name);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session with name " + name + " not found" );
            } else {
                return ResponseEntity.ok(sessions);
            }
        } catch (Exception e) {
            logger.error("ERROR 500: While get Session by Name = "+ name, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: GET Session by Name Request: Server error");
        }
    }
    @GetMapping("/search/bySpeaker")
    public ResponseEntity<?> getSessionsBySpeakerId(@RequestParam Long id) {
        try {
            List<Session> sessions = sessionService.getSessionsBySpeakerId(id);
            if (sessions.isEmpty()) {
                logger.error("ERROR 404: While get session by Speaker ID = {} ",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No sessions found for speaker with ID: " + id);
            }
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            logger.error("ERROR 500: While get Session by Speaker ID = {} - {}" ,id , e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: GET by SpeakerId: Server error");
        }
    }
    @PostMapping("/")
    public ResponseEntity<?> postSession(@Valid @RequestBody Session session, BindingResult result ){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().body(collectValidationErrors(result));
        }
        try {
            Session savedSession = sessionService.postSession(session);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSession);
        } catch (IllegalArgumentException e) { //for validateSessionFields && validateAndAttachSpeakers methods  in service
            logger.error("ERROR 400: While post session  - {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("ERROR 500: While post session {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: POST Session Request: Server error");
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> putSession (@PathVariable Long id,@Valid @RequestBody Session session, BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(collectValidationErrors(result));
        }
        try {
            Session ses = sessionService.putSession(id, session);
            return ResponseEntity.status(HttpStatus.OK).body(ses);
        } catch (SessionNotFoundException e) {
            logger.error("ERROR 404: While put session ID = {} : SessionNotFoundException - {}",id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) { //for validateSessionFields && validateAndAttachSpeakers methods in service
            logger.error("ERROR 400: While put session ID = {} - {}",id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("ERROR 500: While put session ID = {} - {}",id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: PUT Session Request: Server error");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchSession(@PathVariable Long id,@Valid @RequestBody Session session,BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(collectValidationErrors(result));
        }
        try {
            Session ses = sessionService.patchSession(id, session);
            return ResponseEntity.status(HttpStatus.OK).body(ses);
        } catch (SessionNotFoundException e) {
            logger.error("ERROR 404: While patch session ID = {}: SessionNotFoundException - {}",id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) { //for validateSessionFields && validateAndAttachSpeakers methods in service
            logger.error("ERROR 400: While patch session ID = {} - {}",id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("ERROR 500: While patch Session by ID = {} - {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: PATCH Session Request: Server error");
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSessionByID(@PathVariable Long id) {
        try {
            sessionService.deleteSessionById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Sessions " + id + " and associated schedules deleted successfully");
        } catch (SessionNotFoundException e) {
            logger.error("ERROR 404: While delete session ID = {} : SessionNotFoundException - {}",id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("ERROR 500: While delete Session by ID = {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: DELETE Session Request: Server error");
        }
    }

}

