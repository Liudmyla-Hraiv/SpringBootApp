package com.liutyk.first_demo.UnitServiceTests;


import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.repositories.SessionRepository;
import com.liutyk.first_demo.repositories.SessionScheduleRepository;
import com.liutyk.first_demo.repositories.SpeakerRepository;
import com.liutyk.first_demo.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.meta.When;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTests {
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private SpeakerRepository speakerRepository;
    @Mock
    private SessionScheduleRepository sessionScheduleRepository;
    @InjectMocks
    private SessionService sessionService;

    private static Long testSessionID;
    private static Long testSpeakerID;
    private static final Long randomSessionID=15L;
    private static final Long randomSpeakerId=5L;



    @Test
    public void testGetAllSessions(){
        List<Session> sessions= new ArrayList<>();
        sessions.add(new Session());
        sessions.add(new Session());
        when(sessionRepository.findAll()).thenReturn(sessions);
        List<Session> result= sessionService.getAllSessions();
        assertNotNull(result, "The result should not be null");
        assertFalse(result.isEmpty(), "The result list should not be empty");
        assertTrue(result.size()>=2, "The result list should contain at least two sessions");
    }
    @Test
    public void testGetSessionById() {

        Session session = new Session();
        session.setSessionId(randomSessionID);

        when(sessionRepository.findById(randomSessionID)).thenReturn(Optional.of(session));

        Optional<Session> result = sessionService.getSessionById(randomSessionID);
        assertTrue(result.isPresent());
        assertEquals(randomSessionID, result.get().getSessionId());
    }

    @Test
    public void testGetSessionByPartialName() {
        List<Session> sessions= new ArrayList<>();
        Session session1 = new Session();
        session1.setSessionName("Spring Boot Basics");
        sessions.add(session1);
        Session session2 = new Session();
        session2.setSessionName("Advanced Bootcamp");
        sessions.add(session2);
        when(sessionRepository.findBySessionNameContainingIgnoreCase("Boot")).thenReturn(sessions);
        List<Session> result = sessionService.getSessionByPartialName("Boot");
        assertFalse(result.isEmpty(), "There are no sessions with this name");
        assertEquals(2, result.size(), "There should be exactly two sessions returned");
        assertTrue(result.get(0).getSessionName().contains("Boot"), "The session name should contain 'Boot'");
        assertTrue(result.get(1).getSessionName().contains("Boot"), "The session name should contain 'Boot'");
    }
    @Test
    public void testGetSessionBySpeakerID(){

        Speaker speaker = new Speaker();
        speaker.setSpeakerId(randomSpeakerId);
        List<Speaker> speakers = new ArrayList<>();
        speakers.add(speaker);

        List<Session> sessions = new ArrayList<>();
        Session session = new Session();

        session.setSessionId(randomSessionID);
        session.setSessionName("Better Retrospectives");
        session.setSessionDescription("");
        session.setSessionLength(60);
        session.setSpeakers(Collections.singletonList(speaker));
        sessions.add(session);

        when(sessionRepository.findSessionsBySpeakerId(randomSpeakerId)).thenReturn(sessions);
        List<Session> result= sessionService.getSessionsBySpeakerId(randomSpeakerId);

        assertNotNull(result, "The result should not be null");
        assertFalse(result.isEmpty(), "There are no sessions with this speaker id");
        for (Session resSession : result) {
            assertTrue(resSession.getSpeakers().stream()
                            .anyMatch(s -> s.getSpeakerId().equals(randomSpeakerId)),
                    "The session should contain the correct speakerId");
        }
    }
    @Test
    public void testPostSession(){
        Session savedSession=new Session();
        savedSession.setSessionName("Test Session Name 111");
        savedSession.setSessionDescription("New Session Description 111");
        savedSession.setSessionLength(50);
        Speaker tSpeaker= new Speaker();
        tSpeaker.setSpeakerId(randomSpeakerId);
        List<Speaker> speakers =new ArrayList<>();
        speakers.add(tSpeaker);
        savedSession.setSpeakers(speakers);

        when(speakerRepository.findById(randomSpeakerId)).thenReturn(Optional.of(tSpeaker));
        when(sessionRepository.saveAndFlush(any(Session.class))).thenReturn(savedSession);

        Session resultSession=sessionService.postSession(savedSession);
        testSessionID =resultSession.getSessionId();
        assertEquals("Test Session Name 111", resultSession.getSessionName());
        assertEquals("New Session Description 111", resultSession.getSessionDescription());
        assertEquals(50, resultSession.getSessionLength());
        assertEquals(1, resultSession.getSpeakers().size());
        assertEquals(randomSpeakerId, resultSession.getSpeakers().get(0).getSpeakerId());
        verify(sessionRepository).saveAndFlush(savedSession);
    }
    @Test
    public void testPutSession() {
        Session existingSession = new Session();
        existingSession.setSessionId(randomSessionID);
        when(sessionRepository.findById(randomSessionID)).thenReturn(Optional.of(existingSession));
        when(sessionRepository.saveAndFlush(existingSession)).thenReturn(existingSession);

        Session updatedSession = new Session();
        updatedSession.setSessionName("Updated Session Name");
        updatedSession.setSessionDescription("Updated Session Description");
        updatedSession.setSessionLength(41);


        Session result = sessionService.putSession(randomSessionID, updatedSession);
        assertEquals("Updated Session Name", result.getSessionName());
        assertEquals("Updated Session Description", result.getSessionDescription());
        assertEquals(41, result.getSessionLength());
        verify(sessionRepository).saveAndFlush(existingSession);
    }

    @Test
    public void testDeleteById() {
        Session session = new Session();
        session.setSessionId(testSessionID);
        when(sessionRepository.findById(testSessionID)).thenReturn(Optional.of(session));
        doNothing().when(sessionScheduleRepository).deleteBySession_SessionId(testSessionID);
        List<Speaker> speakers = new ArrayList<>();
        when(speakerRepository.findBySessionId(testSessionID)).thenReturn(speakers);
        doNothing().when(sessionRepository).deleteById(testSessionID);

        sessionService.deleteById(testSessionID);

        verify(sessionRepository).deleteById(testSessionID);
    }
}
