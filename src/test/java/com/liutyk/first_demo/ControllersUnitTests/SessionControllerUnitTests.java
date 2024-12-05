package com.liutyk.first_demo.ControllersUnitTests;

import com.liutyk.first_demo.controllers.SessionController;
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
public class SessionControllerUnitTests {
    @Mock
    private SessionService sessionService;
    @InjectMocks
    private SessionController sessionsController;

//Get all sessions
    @Test
    public void testGetAllSessions_Success(){
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(1L);

        Session session1 = new Session(1L, "Session 1", "Description for Session 1", 60, Collections.emptyList(), Collections.emptyList());
        Session session2 = new Session(2L, "Session 2", "Description for Session 2", 90, Collections.singletonList(speaker), Collections.emptyList());

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
    public void testGetAllSessions_NotFound(){
        when(sessionService.getAllSessions()).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = sessionsController.getAllSessions();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("There ara NO any information about session", response.getBody());
    }

//Get sessions by id
    @Test
    public void testGetSessionById_isPresent() {
        Long id=1L;
        Session session = new Session(id, "Session", "Description for Session", 60, Collections.emptyList(), Collections.emptyList());

        when(sessionService.getSessionById(id)).thenReturn(session);
        ResponseEntity<?> response = sessionsController.getSessionById(id);

        Session responseBody = (Session) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(1L, responseBody.getSessionId());
        assertEquals("Session", responseBody.getSessionName());
        assertEquals("Description for Session", responseBody.getSessionDescription());
        assertEquals(60,responseBody.getSessionLength().intValue());
    }
    @Test
    public void testGetSessionById_NotFound() {
        Long id=200L;
        when(sessionService.getSessionById(id)).thenThrow(new SessionNotFoundException(id));
        ResponseEntity<?> response = sessionsController.getSessionById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Session with ID = "+id+" is not found", response.getBody());
    }
//Get sessions by Partial Name
    @Test
    public void testGetSessionsByPartialName_isPresent(){
        Speaker speaker=new Speaker();
        speaker.setSpeakerId(3L);
        Session session1 = new Session(1L, "Session JUnit", "Description for Session 1", 60, Collections.emptyList(), Collections.emptyList());
        Session session2 = new Session(2L, "JUNIT session", "Description for Session 2", 90, Collections.singletonList(speaker), Collections.emptyList());

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
    public void testGetSessionByPartialName_Exception() {
        when(sessionService.getSessionByPartialName("name")).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = sessionsController.getSessionByPartialName("name");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: GET Session by Name Request: Server error", response.getBody());
    }
    @Test
    public void testGetSessionsByPartialName_NotFound(){
        when(sessionService.getSessionByPartialName("abra")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = sessionsController.getSessionByPartialName("abra");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Session with name abra not found", response.getBody());
    }
//Get Sessions by SpeakerId
    @Test
    public void testGetSessionsBySpeakerId_isPresent(){

        Speaker speaker = new Speaker();
        speaker.setSpeakerId(3L);

        Session session1 = new Session(1L, "Session 1", "Description for Session 1", 60,Collections.singletonList(speaker), Collections.emptyList());
        Session session2 = new Session(2L, "Session 2", "Description for Session 2", 90, Collections.singletonList(speaker), Collections.emptyList());

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
    public void testGetSessionsBySpeakerId_NotFound(){
        Long id=190L;
        when(sessionService.getSessionsBySpeakerId(id)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = sessionsController.getSessionsBySpeakerId(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No sessions found for speaker with ID: 190", response.getBody());
    }
    @Test
    public void testGetSessionsBySpeakerId_Exception() {
        when(sessionService.getSessionsBySpeakerId(155L)).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = sessionsController.getSessionsBySpeakerId(155L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: GET by SpeakerId: Server error", response.getBody());
    }
    //POST Sessions
    @Test
    public void testPostSession_Success() {
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(1L);
        Session session = new Session(2L, "Test Session", "Test Description", 90, Collections.singletonList(speaker), Collections.emptyList());

        when(sessionService.postSession(any(Session.class))).thenReturn(session);

        ResponseEntity<?> response = sessionsController.postSession(session);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        Session responseBody = (Session) response.getBody();
        assertEquals("Test Session", responseBody.getSessionName());
        assertEquals(1, responseBody.getSpeakers().size());
    }

    @Test
    public void testPostSession_BadRequest() {
        Session session = new Session();
        session.setSessionName("Test Session");

        ResponseEntity<?> response = sessionsController.postSession(session);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Session must have at least one speaker", response.getBody());
    }

    @Test
    public void testPostSession_Exception() {
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(1L);
        Session session = new Session(2L, "Test Session", "Test Description", 90, Collections.singletonList(speaker), Collections.emptyList());

        when(sessionService.postSession(any(Session.class))).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = sessionsController.postSession(session);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: POST Session Request: Server error", response.getBody());
    }
//PUT Sessions
    @Test
    public void testPutSession_Success() throws SessionNotFoundException {
        Long id=5L;
        Speaker speaker=new Speaker();
        speaker.setSpeakerId(3L);

        Session oldSes =  new Session(2L, "Session 1", "Description for Session 1", 60, Collections.singletonList(speaker), Collections.emptyList());
        Session newSes =  new Session(2L, "Session New", "Description for Session New", 45, Collections.singletonList(speaker), Collections.emptyList());

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
    public void testPutSession_Exception() throws SessionNotFoundException {
        Session session = new Session();
        session.setSessionName("Test Session");
        when(sessionService.putSession(anyLong(), any(Session.class))).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = sessionsController.putSession(333L, session);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: PUT Session Request: Server error", response.getBody());
}
    @Test
    public void testPutSession_NotFound() throws SessionNotFoundException {
        Long id = 15L;
        Session session = new Session();
        session.setSessionId(id);
        when(sessionService.putSession(anyLong(), any(Session.class))).thenThrow(new SessionNotFoundException(id));

        ResponseEntity<?> response = sessionsController.putSession(id, session);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Session with ID = " + id + " is not found", response.getBody());
    }
//PATCH Sessions
@Test
public void testPatchSession_Success() throws SessionNotFoundException {
    Long id=5L;
    Speaker speaker=new Speaker();
    speaker.setSpeakerId(3L);

    Session oldSes =  new Session(2L, "Session 1", "Description for Session 1", 60, Collections.singletonList(speaker), Collections.emptyList());
    Session newSes =  new Session(2L, "Session New", "Description for Session New", 45, Collections.singletonList(speaker), Collections.emptyList());

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
    public void testPatchSession_Exception() throws SessionNotFoundException {
        Session session = new Session();
        session.setSessionName("Test Session");
        when(sessionService.patchSession(anyLong(), any(Session.class))).thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = sessionsController.patchSession(333L, session);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: PATCH Session Request: Server error", response.getBody());
    }
    @Test
    public void testPatchSession_NotFound() throws SessionNotFoundException {
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
    public void testDeleteSession_Success() throws SessionNotFoundException {
        Long id=5L;
        Session session = new Session();
        session.setSessionId(id);
        doNothing().when(sessionService).deleteSessionById(id);
        ResponseEntity<?> response= sessionsController.deleteSessionByID(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sessions " + id + " and associated schedules deleted successfully", response.getBody());
    }

    @Test
    public void testDeleteSession_Exception() throws SessionNotFoundException {
        Long id = 16L;
        Session session = new Session();
        session.setSessionId(id);
        doThrow(new RuntimeException("Server error")).when(sessionService).deleteSessionById(id);

        ResponseEntity<?> response = sessionsController.deleteSessionByID(id);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: DELETE Session Request: Server error", response.getBody());
    }
    @Test
    public void testDeleteSession_NotFound() throws SessionNotFoundException {
        Long id = 15L;
        Session session = new Session();
        session.setSessionId(id);
       doThrow(new SessionNotFoundException(id)).when(sessionService).deleteSessionById(id);

        ResponseEntity<?> response = sessionsController.deleteSessionByID(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Session with ID = " + id + " is not found", response.getBody());
    }
}
