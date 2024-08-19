package com.liutyk.first_demo.ControllersUnitTests;

import com.liutyk.first_demo.controllers.SessionsController;
import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.services.SessionNotFoundException;
import com.liutyk.first_demo.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SessionControllersUnitTests {
    @Mock
    private SessionService sessionService;
    @InjectMocks
    private SessionsController sessionsController;

//Get all sessions
    @Test
    public void testGetAllSessions_NotEmptySessions() throws Exception{
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(1L);

        Session session1 = new Session();
        session1.setSessionId(1L);
        session1.setSessionName("Session 1");
        session1.setSessionDescription("Description for Session 1");
        session1.setSessionLength(60);

        Session session2 = new Session();
        session2.setSessionId(2L);
        session2.setSessionName("Session 2");
        session2.setSessionDescription("Description for Session 2");
        session2.setSessionLength(90);
        session2.setSpeakers(Collections.singletonList(speaker));

        List<Session> sessions = Arrays.asList(session1, session2);
        when(sessionService.getAllSessions()).thenReturn(sessions);

        ResponseEntity<?> response = sessionsController.getAllSessions();
        List<Session> responseBody= (List<Session>)response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
        assertEquals(1L, responseBody.get(0).getSessionId());
        assertEquals("Session 1", responseBody.get(0).getSessionName());
        assertEquals("Description for Session 1", responseBody.get(0).getSessionDescription());
        assertEquals(60, responseBody.get(0).getSessionLength().intValue());

        assertEquals(2L, responseBody.get(1).getSessionId());
        assertEquals("Session 2", responseBody.get(1).getSessionName());
        assertEquals("Description for Session 2", responseBody.get(1).getSessionDescription());
        assertEquals(90, responseBody.get(1).getSessionLength().intValue());
        assertEquals(1L, responseBody.get(1).getSpeakers().get(0).getSpeakerId());

    }
    @Test
    public void testGetAllSessions_isEmpty() throws Exception{
        when(sessionService.getAllSessions()).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = sessionsController.getAllSessions();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("There ara NO any information about session", response.getBody());
    }

//Get sessions by id
    @Test
    public void testGetSessionById_SessionPresent() throws Exception{
        Session session = new Session();
        session.setSessionId(1L);
        session.setSessionName("Session");
        session.setSessionDescription("Description for Session");
        session.setSessionLength(60);

        when(sessionService.getSessionById(1L)).thenReturn(Optional.of(session));
        ResponseEntity<?> response = sessionsController.getSessionById(1L);
        Optional<Session> responseBody = (Optional<Session>) response.getBody();
        Session actualSession = responseBody.get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(responseBody.isPresent());
        assertEquals(1L, actualSession.getSessionId());
        assertEquals("Session", actualSession.getSessionName());
        assertEquals("Description for Session", actualSession.getSessionDescription());
        assertEquals(60,actualSession.getSessionLength().intValue());
    }
    @Test
    public void testGetSessionById_SessionAbsent() throws Exception{
        when(sessionService.getSessionById(188L)).thenReturn(Optional.empty());
        ResponseEntity<?> response = sessionsController.getSessionById(188L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Session Not found", response.getBody());
    }
//Get sessions by Partial Name
    @Test
    public void testGetSessionsByPartialName_NamePresent() throws Exception{
        Session session1 = new Session();
        session1.setSessionId(1L);
        session1.setSessionName("Session JUnit");
        session1.setSessionDescription("Description for Session 1");
        session1.setSessionLength(60);

        Session session2 = new Session();
        session2.setSessionId(2L);
        session2.setSessionName("JUNIT session");
        session2.setSessionDescription("Description for Session 2");
        session2.setSessionLength(90);

        List<Session> sessions = Arrays.asList(session1, session2);
        when(sessionService.getSessionByPartialName("junit")).thenReturn(sessions);

        ResponseEntity<?> response = sessionsController.getSessionByPartialName("junit");
        List<Session> responseBody= (List<Session>)response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
        assertTrue(responseBody.get(0).getSessionName().contains("JUnit"));
        assertTrue(responseBody.get(1).getSessionName().contains("JUNIT"));

    }
    @Test
    public void testGetSessionByPartialName_RuntimeException() {
        when(sessionService.getSessionByPartialName("")).thenThrow(new RuntimeException("Database Error"));

        ResponseEntity<?> response = sessionsController.getSessionByPartialName("");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: GET Session by Name Request: Database Error", response.getBody());
    }
    @Test
    public void testGetSessionsByPartialName_NameAbsent() throws Exception{
        when(sessionService.getSessionByPartialName("abra")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = sessionsController.getSessionByPartialName("abra");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Session with name abra not found", response.getBody());
    }
//Get Sessions by SpeakerId
    @Test
    public void testGetSessionsBySpeakerId_SpeakerPresent() throws Exception{
        Speaker speaker=new Speaker();
        speaker.setSpeakerId(3L);

        Session session1 = new Session();
        session1.setSessionId(1L);
        session1.setSessionName("Session 1");
        session1.setSessionDescription("Description for Session 1");
        session1.setSessionLength(60);
        session1.setSpeakers(Collections.singletonList(speaker));


        Session session2 = new Session();
        session2.setSessionId(2L);
        session2.setSessionName("Session 2");
        session2.setSessionDescription("Description for Session 2");
        session2.setSessionLength(90);
        session2.setSpeakers(Collections.singletonList(speaker));

        List<Session> sessions = Arrays.asList(session1, session2);
        when(sessionService.getSessionsBySpeakerId(3L)).thenReturn(sessions);

        ResponseEntity<?> response = sessionsController.getSessionsBySpeakerId(3L);
        List<Session> responseBody= (List<Session>)response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
        assertEquals(3L, responseBody.get(0).getSpeakers().get(0).getSpeakerId());
        assertEquals(3L, responseBody.get(1).getSpeakers().get(0).getSpeakerId());
    }
    @Test
    public void testGetSessionsBySpeakerId_SpeakerAbsent(){
        when(sessionService.getSessionsBySpeakerId(190L)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = sessionsController.getSessionsBySpeakerId(190L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No sessions found for speaker with ID: 190", response.getBody());
    }
    @Test
    public void testGetSessionsBySpeakerId_RuntimeException() {
        when(sessionService.getSessionsBySpeakerId(155L)).thenThrow(new RuntimeException("Database Error"));

        ResponseEntity<?> response = sessionsController.getSessionsBySpeakerId(155L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: GET by SpeakerId: Database Error", response.getBody());
    }
    //POST Sessions
    @Test
    public void testPostSession_ValidSession() {
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(1L);

        Session session = new Session();
        session.setSessionName("Test Session");
        session.setSessionDescription("Test Description");
        session.setSessionLength(60);
        session.setSpeakers(Collections.singletonList(speaker));

        when(sessionService.postSession(any(Session.class))).thenReturn(session);

        ResponseEntity<?> response = sessionsController.postSession(session);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        Session responseBody = (Session) response.getBody();
        assertEquals("Test Session", responseBody.getSessionName());
        assertEquals(1, responseBody.getSpeakers().size());
    }

    @Test
    public void testPostSession_NoSpeakers() {
        // Створення тестової сесії без спікерів
        Session session = new Session();
        session.setSessionName("Test Session");

        // Виклик методу контролера
        ResponseEntity<?> response = sessionsController.postSession(session);

        // Перевірка результатів
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Session must have at least one speaker", response.getBody());
    }

    @Test
    public void testPostSession_RuntimeException() {
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(1L);

        Session session = new Session();
        session.setSessionName("Test Session");
        session.setSessionDescription("Test Description");
        session.setSessionLength(60);
        session.setSpeakers(Collections.singletonList(speaker));

        when(sessionService.postSession(any(Session.class))).thenThrow(new RuntimeException("Database Error"));

        ResponseEntity<?> response = sessionsController.postSession(session);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: POST Session Request: Database Error", response.getBody());
    }
//PUT Sessions
    @Test
    public void testPutSession_ValidSession() throws SessionNotFoundException {
        Long id=5L;
        Speaker speaker=new Speaker();
        speaker.setSpeakerId(3L);

        Session oldSes = new Session();
        oldSes.setSessionId(id);
        oldSes.setSessionName("Session 1");
        oldSes.setSessionDescription("Description for Session 1");
        oldSes.setSessionLength(60);
        oldSes.setSpeakers(Collections.singletonList(speaker));


        Session newSes = new Session();
        newSes.setSessionName("Session New");
        newSes.setSessionDescription("Description for Session New");
        newSes.setSessionLength(45);
        newSes.setSpeakers(Collections.singletonList(speaker));
        when(sessionService.putSession(id, oldSes)).thenReturn(newSes);
        ResponseEntity<?> response= sessionsController.putSession(id, oldSes);
        Session responseBody= (Session) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("Session New", responseBody.getSessionName());
        assertEquals("Description for Session New", responseBody.getSessionDescription());
        assertEquals(45, responseBody.getSessionLength());
    }

    @Test
    public void testPutSession_RuntimeException() throws SessionNotFoundException {
        Session session = new Session();
        session.setSessionName("Test Session");
        when(sessionService.putSession(anyLong(), any(Session.class))).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = sessionsController.putSession(333L, session);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: PUT Session Request: Server error", response.getBody());
}
    @Test
    public void testPutSession_SessionNotFoundException() throws SessionNotFoundException {
        Long id = 15L;
        Session session = new Session();
        session.setSessionName("Test Session");
        when(sessionService.putSession(anyLong(), any(Session.class))).thenThrow(new SessionNotFoundException(id));

        ResponseEntity<?> response = sessionsController.putSession(id, session);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Session with ID = " + id + " is not found", response.getBody());
    }
//PATCH Sessions
@Test
public void testPatchSession_ValidSession() throws SessionNotFoundException {
    Long id=5L;
    Speaker speaker=new Speaker();
    speaker.setSpeakerId(3L);

    Session oldSes = new Session();
    oldSes.setSessionId(id);
    oldSes.setSessionName("Session 1");
    oldSes.setSessionDescription("Description for Session 1");
    oldSes.setSessionLength(60);


    Session newSes = new Session();
    newSes.setSessionName("Session New");
    newSes.setSessionDescription("Description for Session New");
    newSes.setSessionLength(45);
    when(sessionService.patchSession(id, oldSes)).thenReturn(newSes);
    ResponseEntity<?> response= sessionsController.patchSession(id, oldSes);
    Session responseBody= (Session) response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(responseBody);
    assertEquals("Session New", responseBody.getSessionName());
    assertEquals("Description for Session New", responseBody.getSessionDescription());
    assertEquals(45, responseBody.getSessionLength());
}

    @Test
    public void testPatchSession_RuntimeException() throws SessionNotFoundException {
        Session session = new Session();
        session.setSessionName("Test Session");
        when(sessionService.patchSession(anyLong(), any(Session.class))).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = sessionsController.patchSession(333L, session);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: PATCH Session Request: Server error", response.getBody());
    }
    @Test
    public void testPatchSession_SessionNotFoundException() throws SessionNotFoundException {
        Long id = 15L;
        Session session = new Session();
        session.setSessionName("Test Session");
        when(sessionService.patchSession(anyLong(), any(Session.class))).thenThrow(new SessionNotFoundException(id));

        ResponseEntity<?> response = sessionsController.patchSession(id, session);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Session with ID = " + id + " is not found", response.getBody());
    }
//DELETE Sessions

    @Test
    public void testDeleteSession_ValidSession() throws SessionNotFoundException {
        Long id=5L;
        Session session = new Session();
        session.setSessionId(id);
        doNothing().when(sessionService).deleteById(id);
        ResponseEntity<?> response= sessionsController.deleteByID(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sessions " + id + " and associated schedules deleted successfully", response.getBody());
    }

    @Test
    public void testDeleteSession_RuntimeException() throws SessionNotFoundException {
        Long id = 16L;
        Session session = new Session();
        session.setSessionId(id);
        doThrow(new RuntimeException("Server error")).when(sessionService).deleteById(id);

        ResponseEntity<?> response = sessionsController.deleteByID(id);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: DELETE Session Request: Server error", response.getBody());
    }
    @Test
    public void testDeleteSession_SessionNotFoundException() throws SessionNotFoundException {
        Long id = 15L;
        Session session = new Session();
        session.setSessionId(id);
       doThrow(new SessionNotFoundException(id)).when(sessionService).deleteById(id);

        ResponseEntity<?> response = sessionsController.deleteByID(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());


        assertEquals("Session with ID = " + id + " is not found", response.getBody());
    }
}
