package com.liutyk.first_demo.ServicesUnitTests;


import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.repositories.SessionRepository;
import com.liutyk.first_demo.repositories.SessionScheduleRepository;
import com.liutyk.first_demo.repositories.SpeakerRepository;
import com.liutyk.first_demo.services.SessionNotFoundException;
import com.liutyk.first_demo.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceUnitTests {
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private SpeakerRepository speakerRepository;
    @Mock
    private SessionScheduleRepository sessionScheduleRepository;
    @InjectMocks
    private SessionService sessionService;

     Long testSessionID=70L;
     Long randomSessionID=15L;
     Long randomSpeakerId=5L;


//GET ALL SESSIONS
    @Test
    public void testGetAllSessions(){
        List<Session> sessions= new ArrayList<>();
        sessions.add(new Session());
        sessions.add(new Session());
        when(sessionRepository.findAll()).thenReturn(sessions);
        List<Session> result= sessionService.getAllSessions();
        assertNotNull(result, "The result should not be null");
        assertFalse(result.isEmpty(), "The result list should not be empty");
        assertTrue(result.size()>=2, "The result list should contain at least 2 sessions");
    }

    @Test
    public void testGetSessionById() {

        Session session = new Session();
        session.setSessionId(randomSessionID);

        when(sessionRepository.findById(randomSessionID)).thenReturn(Optional.of(session));

        Session result = sessionService.getSessionById(randomSessionID);
        assertNotNull(result);
        assertEquals(randomSessionID, result.getSessionId());
    }

    @Test
    public void testGetSessionByPartialName() {
        List<Session> sessions= new ArrayList<>();
        Session session1 = new Session();
        session1.setSessionName("Spring BOOT Basics");
        sessions.add(session1);
        Session session2 = new Session();
        session2.setSessionName("Advanced Bootcamp");
        sessions.add(session2);
        when(sessionRepository.getBySessionNameContainingIgnoreCase("boot")).thenReturn(sessions);
        List<Session> result = sessionService.getSessionByPartialName("boot");
        assertFalse(result.isEmpty(), "There are no sessions with this name");
        assertEquals(2, result.size(), "There should be exactly two sessions returned");
        assertTrue(result.get(0).getSessionName().contains("BOOT"), "The session name should contain 'Boot'");
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

        when(sessionRepository.getSessionsBySpeakerId(randomSpeakerId)).thenReturn(sessions);
        List<Session> result= sessionService.getSessionsBySpeakerId(randomSpeakerId);

        assertNotNull(result, "The result should not be null");
        assertFalse(result.isEmpty(), "There are no sessions with this speaker id");
        assertEquals(randomSpeakerId, result.get(0).getSpeakers().get(0).getSpeakerId());
    }
    @Test
    public void testPostSession(){
        Speaker speaker= new Speaker();
        speaker.setSpeakerId(randomSpeakerId);
        List<Speaker> speakers =new ArrayList<>();
        speakers.add(speaker);

        Session savedSession=new Session();
        savedSession.setSessionName("Test Session Name 111");
        savedSession.setSessionDescription("New Session Description 111");
        savedSession.setSessionLength(50);
        savedSession.setSpeakers(Collections.singletonList(speaker));

        when(speakerRepository.findById(randomSpeakerId)).thenReturn(Optional.of(speaker));
        when(sessionRepository.saveAndFlush(savedSession)).thenReturn(savedSession);

        Session resultSession=sessionService.postSession(savedSession);
        assertEquals("Test Session Name 111", resultSession.getSessionName());
        assertEquals("New Session Description 111", resultSession.getSessionDescription());
        assertEquals(50, resultSession.getSessionLength());
        assertEquals(1, resultSession.getSpeakers().size());
        assertEquals(randomSpeakerId, resultSession.getSpeakers().get(0).getSpeakerId());
        verify(sessionRepository).saveAndFlush(savedSession);
    }
    @Test
    public void testPutSession() throws SessionNotFoundException {
        Speaker speaker= new Speaker();
        speaker.setSpeakerId(randomSpeakerId);
        speaker.setFirstName("FirstName");
        speaker.setLastName("LastName");
        List<Speaker> speakers =new ArrayList<>();
        speakers.add(speaker);

        Session existingSession = new Session();
        existingSession.setSessionId(randomSessionID);
        existingSession.setSessionName("Session Name");
        existingSession.setSessionDescription("Description");
        existingSession.setSessionLength(60);
        existingSession.setSpeakers(Collections.singletonList(speaker));

        when(sessionRepository.findById(randomSessionID)).thenReturn(Optional.of(existingSession));
        when(sessionRepository.saveAndFlush(existingSession)).thenReturn(existingSession);

        Session updatedSession = new Session();
        updatedSession.setSessionName("PUT Session Name");
        updatedSession.setSessionDescription("PUT Session Description");
        updatedSession.setSessionLength(41);
        updatedSession.setSpeakers(Collections.singletonList(speaker));

        Session result = sessionService.putSession(randomSessionID, updatedSession);
        assertEquals("PUT Session Name", result.getSessionName());
        assertEquals("PUT Session Description", result.getSessionDescription());
        assertEquals(41, result.getSessionLength());
        verify(sessionRepository).saveAndFlush(existingSession);
    }

    @Test
    public void testPatchSession() throws SessionNotFoundException {
        List<Speaker> speakers = new ArrayList<>();
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(randomSpeakerId);
        speakers.add(speaker);

        Session existingSession = new Session();
        existingSession.setSessionId(randomSessionID);
        existingSession.setSessionName("Original Name");
        existingSession.setSessionDescription("Original Description");
        existingSession.setSessionLength(45);
        existingSession.setSpeakers(Collections.singletonList(speaker));

        when(sessionRepository.findById(randomSessionID)).thenReturn(Optional.of(existingSession));
        when(sessionRepository.saveAndFlush(existingSession)).thenReturn(existingSession);

        Session updatedSession = new Session();
        updatedSession.setSessionName("PATCH Session");
//        updatedSession.setSessionDescription("PATCH Description");

        Session result = sessionService.patchSession(randomSessionID, updatedSession);
        assertEquals("PATCH Session", result.getSessionName());
        assertEquals("Original Description", result.getSessionDescription());
        assertEquals(45, result.getSessionLength());
        assertEquals(randomSpeakerId, result.getSpeakers().get(0).getSpeakerId());

        verify(sessionRepository).saveAndFlush(existingSession);
    }
    @Test
    public void testDeleteSessionById() throws SessionNotFoundException {
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(randomSpeakerId);
        speaker.setSessions(new ArrayList<>());
        List<Speaker> speakers = new ArrayList<>();
        speakers.add(speaker);
        Session session = new Session();
        session.setSessionId(testSessionID);
        session.setSpeakers(Collections.singletonList(speaker));
        speaker.getSessions().add(session);

        when(sessionRepository.findById(testSessionID)).thenReturn(Optional.of(session));
        //delete session schedule
        doNothing().when(sessionScheduleRepository).deleteBySession_SessionId(testSessionID);
        //cut and save speaker
        when(speakerRepository.getSpeakerBySessionId(testSessionID)).thenReturn(speakers);
        when(speakerRepository.save(speaker)).thenReturn(speaker);
        //delete session
        doNothing().when(sessionRepository).deleteById(testSessionID);
        sessionService.deleteSessionById(testSessionID);
        //Check all out actions
        assertTrue(speaker.getSessions().isEmpty(), "The session list in speaker should be empty after deletion");
        verify(sessionScheduleRepository).deleteBySession_SessionId(testSessionID);
        verify(speakerRepository).save(speaker);
        verify(sessionRepository).deleteById(testSessionID);
    }
}
