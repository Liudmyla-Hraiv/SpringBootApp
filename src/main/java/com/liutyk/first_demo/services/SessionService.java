package com.liutyk.first_demo.services;

import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.repositories.SessionRepository;
import com.liutyk.first_demo.repositories.SessionScheduleRepository;
import com.liutyk.first_demo.repositories.SpeakerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final SpeakerRepository speakerRepository;
    private final SessionScheduleRepository sessionScheduleRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository,
                          SpeakerRepository speakerRepository,
                          SessionScheduleRepository sessionScheduleRepository) {
        this.sessionRepository = sessionRepository;
        this.speakerRepository = speakerRepository;
        this.sessionScheduleRepository = sessionScheduleRepository;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    public List<Session> getSessionByPartialName(String name) {
        return sessionRepository.findBySessionNameContainingIgnoreCase(name);
    }

    public List<Session> getSessionsBySpeakerId(Long id) {
        return sessionRepository.findSessionsBySpeakerId(id);
    }

    public Session createSession(Session session) {
        if (!session.getSpeakers().isEmpty()) {
            List<Speaker> attachedSpeaker = new ArrayList<>();
            for (Speaker speaker : session.getSpeakers()) {
                Optional<Speaker> optional = speakerRepository.findById(speaker.getSpeakerId());
                optional.ifPresent(attachedSpeaker::add);
            }
            session.setSpeakers(attachedSpeaker);
        }
        return sessionRepository.saveAndFlush(session);
    }

    public Session updateSession(Long id, Session session) {
        Optional<Session> optionalSession = sessionRepository.findById(id);
        if (optionalSession.isEmpty()) {
            throw new RuntimeException("Session with ID = " + id + " is not found");
        }
        Session existingSession = optionalSession.get();
        existingSession.setSessionName(session.getSessionName());
        existingSession.setSessionDescription(session.getSessionDescription());
        existingSession.setSessionLength(session.getSessionLength());
        if (session.getSpeakers() != null && !session.getSpeakers().isEmpty()) {
            List<Speaker> speakers = speakerRepository.findBySessionId(id);
            existingSession.setSpeakers(speakers);
        }
        return sessionRepository.saveAndFlush(existingSession);
    }

    public Session modifySession(Long id, Session session) {
        Optional<Session> optional = sessionRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Session with ID " + id + " not found");
        }
        Session existingSession = optional.get();
        if (session.getSessionName() != null && !session.getSessionName().isEmpty()) {
            existingSession.setSessionName(session.getSessionName());
        }
        if (session.getSessionDescription() != null && !session.getSessionDescription().isEmpty()) {
            existingSession.setSessionDescription(session.getSessionDescription());
        }
        if (session.getSessionLength() != null) {
            existingSession.setSessionLength(session.getSessionLength());
        }
        return sessionRepository.saveAndFlush(existingSession);
    }

    public void deleteById(Long id) {
        Optional<Session> optionalSession = sessionRepository.findById(id);
        if (optionalSession.isEmpty()) {
            throw new RuntimeException("Session " + id + " does not exist");
        }
        Session session = optionalSession.get();
        sessionScheduleRepository.deleteBySessionId_SessionId(id);
        List<Speaker> speakers = speakerRepository.findBySessionId(id);
        for (Speaker speaker : speakers) {
            speaker.getSessions().remove(session);
            speakerRepository.save(speaker);
        }
        sessionRepository.deleteById(id);
    }
}
