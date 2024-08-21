package com.liutyk.first_demo.ControllersUnitTests;

import com.liutyk.first_demo.controllers.SessionSpeakerController;
import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.models.SessionSpeaker;
import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.services.SessionSpeakersServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SessionSpeakerControllerUnitTests {
    @Mock
    private SessionSpeakersServer sessionSpeakersServer;
    @InjectMocks
    private SessionSpeakerController sessionSpeakerController;
//GET ALL
    @Test
    public void testGetAllSessionSpeakers_Success(){
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(3L);

        Session session1 = new Session(1L, "Session 1", "Description for Session 1", 60, Collections.emptyList(), Collections.emptyList());
        Session session2 = new Session(2L, "Session 2", "Description for Session 2", 90, Collections.singletonList(speaker), Collections.emptyList());
        SessionSpeaker sessionSpeaker1 = new SessionSpeaker(session1,speaker);
        SessionSpeaker sessionSpeaker2 = new SessionSpeaker(session2,speaker);
        List<SessionSpeaker> list= List.of(sessionSpeaker1, sessionSpeaker2);
        when(sessionSpeakersServer.getAllSessionSpeakers()).thenReturn(list);
        ResponseEntity<?> response= sessionSpeakerController.getAllSessionSpeakers();
        List<SessionSpeaker> resBody=(List<SessionSpeaker>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(resBody);
        assertEquals(3L, resBody.get(0).getSpeaker().getSpeakerId());
        assertEquals(1L, resBody.get(0).getSession().getSessionId());
    }
    @Test
    public void testGetAllSessionSpeakers_NotFound(){
        when(sessionSpeakersServer.getAllSessionSpeakers()).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = sessionSpeakerController.getAllSessionSpeakers();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No Data",response.getBody());

    }
    @Test
    public void testGetAllSessionSpeakers_Exception(){
        when(sessionSpeakersServer.getAllSessionSpeakers()).thenThrow(new RuntimeException("Server error"));
        ResponseEntity<?> response= sessionSpeakerController.getAllSessionSpeakers();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: SpeakerSession GET ALL: Server error", response.getBody());
    }
//Find by Session ID
    @Test
    public void testGetBySessionId_Success(){
        Long id=1L;
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(3L);
        Session session1 = new Session(id, "Session 1", "Description for Session 1", 60, Collections.emptyList(), Collections.emptyList());
        SessionSpeaker sessionSpeaker1 = new SessionSpeaker(session1,speaker);
        List<SessionSpeaker> list= List.of(sessionSpeaker1);
        when(sessionSpeakersServer.getBySessionId(id)).thenReturn(list);
        ResponseEntity<?> response= sessionSpeakerController.getBySessionId(id);
        List<SessionSpeaker> resBody=(List<SessionSpeaker>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(resBody);
        assertEquals(1L, resBody.get(0).getSession().getSessionId());
        assertEquals(3L, resBody.get(0).getSpeaker().getSpeakerId());

    }
    @Test
    public void testGetBySessionId_NotFound(){
        Long id=200L;
        when(sessionSpeakersServer.getBySessionId(id)).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = sessionSpeakerController.getBySessionId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("SessionSpeakers  don't found by Session ID = " + id ,response.getBody());
    }
    @Test
    public void testGetBySessionId_Exception(){
        when(sessionSpeakersServer.getBySessionId(200L)).thenThrow(new RuntimeException("Server error"));
        ResponseEntity<?> response= sessionSpeakerController.getBySessionId(200L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR while search SessionSpeakers by Session ID: Server error", response.getBody());
    }

 //Find by Speaker ID
    @Test
    public void testGetBySpeakerId_Success(){
        Long id=1L;
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(id);
        Session session1 = new Session(3L, "Session 1", "Description for Session 1", 60, Collections.emptyList(), Collections.emptyList());
        SessionSpeaker sessionSpeaker1 = new SessionSpeaker(session1,speaker);
        List<SessionSpeaker> list= List.of(sessionSpeaker1);
        when(sessionSpeakersServer.getBySpeakerId(id)).thenReturn(list);
        ResponseEntity<?> response= sessionSpeakerController.getBySpeakerId(id);
        List<SessionSpeaker> resBody=(List<SessionSpeaker>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(resBody);
        assertEquals(3L, resBody.get(0).getSession().getSessionId());
        assertEquals(1L, resBody.get(0).getSpeaker().getSpeakerId());
 }
    @Test
    public void testGetBySpeakerId_NotFound(){
        Long id=200L;
        when(sessionSpeakersServer.getBySpeakerId(id)).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = sessionSpeakerController.getBySpeakerId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("SessionSpeakers  don't found by Speaker ID = " + id ,response.getBody());
    }
    @Test
    public void testGetBySpeakerId_Exception(){
        when(sessionSpeakersServer.getBySpeakerId(200L)).thenThrow(new RuntimeException("Server error"));
        ResponseEntity<?> response= sessionSpeakerController.getBySpeakerId(200L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR while search SessionSpeakers by Speaker ID: Server error", response.getBody());
    }
}
