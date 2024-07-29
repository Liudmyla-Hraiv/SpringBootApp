package com.liutyk.first_demo.controllers;

import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.repositories.SessionRepository;
import com.liutyk.first_demo.repositories.SessionScheduleRepository;
import com.liutyk.first_demo.repositories.SpeakerRepository;
import com.liutyk.first_demo.repositories.SessionSpeakersRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/sessions")
public class SessionsController {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionScheduleRepository sessionScheduleRepository;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private SessionSpeakersRepository sessionSpeakersRepository;

    @GetMapping
    public ResponseEntity<?> list(){
        List<Session> allSessions = sessionRepository.findAll();
        if(allSessions.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There ara NO any information about session");
        }
        else return ResponseEntity.ok(allSessions);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<?> getSessionById(@PathVariable Long id){
       Optional<Session> getSession = sessionRepository.findById(id);
        if (getSession.isPresent()) {
            return ResponseEntity.ok(getSession);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }
    }

    @GetMapping("/search/ByName")
    public ResponseEntity<?> getSessionByPartialName(@RequestParam String name) {
        try {
            List<Session> sessions = sessionRepository.findBySessionNameContainingIgnoreCase(name);
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
            List<Session> sessions = sessionRepository.findSessionsBySpeakerId(id);
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
            session.setSessionName(session.getSessionName());
            session.setSessionDescription(session.getSessionDescription());
            session.setSessionLength(session.getSessionLength());
            if (!session.getSpeakers().isEmpty()){
                List<Speaker> attachedSpeaker= new ArrayList<>();
                for (Speaker speaker: session.getSpeakers()){
                    Optional<Speaker> optional= speakerRepository.findById(speaker.getSpeakerId());
                    if(optional.isPresent()){
                        attachedSpeaker.add(optional.get());
                    }
                    else {
                        ResponseEntity.status(HttpStatus.NOT_FOUND).body("Speaker with ID " + speaker + " not found");
                    }
                }
                session.setSpeakers(attachedSpeaker);
            }
                Session ses =sessionRepository.saveAndFlush(session);
            return ResponseEntity.status(201).body(ses);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: POST Session Request " +e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSession (@PathVariable Long id, @RequestBody Session session){
            try {
                Optional<Session> optionalSession = sessionRepository.findById(id);
                if (optionalSession.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session with ID = " + id + " is not found");
                } else{
                    Session existingSession=optionalSession.get();
                    //"session_id" is a primary key, and we don't want to replace it.
                    BeanUtils.copyProperties(session, existingSession, "sessionId", "speakers","schedules");
                    if (session.getSpeakers()!=null && !session.getSpeakers().isEmpty()) {
                        List<Speaker> speakers = speakerRepository.findBySessionId(id);
                        existingSession.setSpeakers(speakers);
                    }
                    Session ses = sessionRepository.saveAndFlush(existingSession);
                    return ResponseEntity.status(HttpStatus.OK).body(ses);
                    }
            }catch (Exception e)
            {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: PUT Session Request"+ e.getMessage());
            }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> modifySession(@PathVariable Long id, @RequestBody Session session){
       try{
           Optional<Session> optional = sessionRepository.findById(id);
           if (optional.isEmpty()){
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session with ID " + id + " not found");
           }
           Session existingSession= optional.get();
           if (session.getSessionName()!=null && !session.getSessionName().isEmpty()) {
               existingSession.setSessionName(session.getSessionName());}

           if (session.getSessionDescription()!=null && !session.getSessionDescription().isEmpty()) {
               existingSession.setSessionDescription(session.getSessionDescription());}

           if (session.getSessionLength()!=null) {existingSession.setSessionLength(session.getSessionLength());}

           Session ses = sessionRepository.saveAndFlush(existingSession);
           return ResponseEntity.status(HttpStatus.OK).body(ses);
       }catch (Exception e){
           e.printStackTrace();
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: PATCH Session Request "+ e.getMessage() );
       }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteByID(@PathVariable Long id) {
        try{
            Optional<Session> optionalSession =sessionRepository.findById(id);
            if (!optionalSession.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session " + id + " does not exist");
            } else {
                Session session =optionalSession.get();
                sessionScheduleRepository.deleteBySessionId_SessionId(id);
                List<Speaker> speakers = speakerRepository.findBySessionId(id);
                //Delete session, but keep speakers
                for (Speaker speaker: speakers){
                    speaker.getSessions().remove(session);
                    speakerRepository.save(speaker);
                }

                sessionRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body("Sessions "+ id+ " and associated schedules deleted successfully");
            }
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: DELETE Session Request " +e.getMessage());
        }
    }
}

