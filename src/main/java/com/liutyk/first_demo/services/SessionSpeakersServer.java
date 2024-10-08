package com.liutyk.first_demo.services;

import com.liutyk.first_demo.models.SessionSpeaker;
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
    public List<SessionSpeaker> getAllSessionSpeakers(){
        return sessionSpeakersRepository.findAll();
    }

    public List<SessionSpeaker> getBySessionId(Long sessionId){
        return sessionSpeakersRepository.getBySessionId(sessionId);
    }
    public List<SessionSpeaker> getBySpeakerId(Long speakerId){
        return sessionSpeakersRepository.getBySpeakerId(speakerId);
    }


}
