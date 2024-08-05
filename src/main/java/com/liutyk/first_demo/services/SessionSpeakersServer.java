package com.liutyk.first_demo.services;

import com.liutyk.first_demo.models.SessionSpeakers;
import com.liutyk.first_demo.repositories.SessionSpeakersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionSpeakersServer {
    private final SessionSpeakersRepository sessionSpeakersRepository;
    @Autowired
    public SessionSpeakersServer(SessionSpeakersRepository sessionSpeakersRepository) {
        this.sessionSpeakersRepository = sessionSpeakersRepository;
    }
    public List<SessionSpeakers> getAllSessionSpeakers(){
        return sessionSpeakersRepository.findAll();
    }
    public List<SessionSpeakers> findBySessionId(Long sessionId){
        return sessionSpeakersRepository.findBySessionId(sessionId);
    }
    public List<SessionSpeakers> findBySpeakerId(Long speakerId){
        return sessionSpeakersRepository.findBySpeakerId(speakerId);
    }


}
