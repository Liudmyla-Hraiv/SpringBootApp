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

    public Session getSessionById(Long id) throws SessionNotFoundException {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new SessionNotFoundException(id));
    }

    public List<Session> getSessionByPartialName(String name) {
        return sessionRepository.getBySessionNameContainingIgnoreCase(name);
    }

    public List<Session> getSessionsBySpeakerId(Long id) {
        return sessionRepository.getSessionsBySpeakerId(id);
    }

    public Session postSession(Session session) {
            List<Speaker> attachedSpeaker = new ArrayList<>();
            for (Speaker speaker : session.getSpeakers()) {
                Optional<Speaker> optional = speakerRepository.findById(speaker.getSpeakerId());
                optional.ifPresent(attachedSpeaker::add);
            }
            session.setSpeakers(attachedSpeaker);
        return sessionRepository.saveAndFlush(session);
    }

    public Session putSession(Long id, Session session) throws SessionNotFoundException {
        Optional<Session> optionalSession = sessionRepository.findById(id);
        if (optionalSession.isEmpty()) {
            throw new SessionNotFoundException(id);
        }
        Session existingSession = optionalSession.get();
        existingSession.setSessionName(session.getSessionName());
        existingSession.setSessionDescription(session.getSessionDescription());
        existingSession.setSessionLength(session.getSessionLength());
        if (session.getSpeakers() != null && !session.getSpeakers().isEmpty()) {
            List<Speaker> speakers = speakerRepository.getSpeakerBySessionId(id);
            existingSession.setSpeakers(speakers);
        }
        return sessionRepository.saveAndFlush(existingSession);
    }

    public Session patchSession(Long id, Session session) throws SessionNotFoundException{
        Optional<Session> optional = sessionRepository.findById(id);
        if (optional.isEmpty()) {
            throw new SessionNotFoundException (id);
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

    public void deleteSessionById(Long id) throws SessionNotFoundException {
        Optional<Session> optionalSession = sessionRepository.findById(id);
        if (optionalSession.isEmpty()) {
            throw new SessionNotFoundException(id);
        }
        Session session = optionalSession.get();
        sessionScheduleRepository.deleteBySession_SessionId(id);
        List<Speaker> speakers = speakerRepository.getSpeakerBySessionId(id);
        for (Speaker speaker : speakers) {
            speaker.getSessions().remove(session);
            speakerRepository.save(speaker);
        }
        sessionRepository.deleteById(id);
    }
}
