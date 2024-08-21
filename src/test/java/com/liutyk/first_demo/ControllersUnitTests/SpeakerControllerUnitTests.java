package com.liutyk.first_demo.ControllersUnitTests;

import com.liutyk.first_demo.controllers.SpeakerController;
import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.services.SpeakerNotFoundException;
import com.liutyk.first_demo.services.SpeakerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.*;


@ExtendWith(MockitoExtension.class)
public class SpeakerControllerUnitTests {
    @Mock
    private SpeakerService speakerService;
    @InjectMocks
    private SpeakerController speakerController;
//GET all speakers
    @Test
    public void testGetAllSpeakers_Success(){
        Speaker speaker1 =new Speaker();
        speaker1.setSpeakerId(1L);
        Speaker speaker2=new Speaker();
        speaker2.setSpeakerId(2L);

        List<Speaker> speakers = Arrays.asList(speaker1, speaker2);
        when(speakerService.getAllSpeakers()).thenReturn(speakers);

        ResponseEntity<?> response = speakerController.getAllSpeakers();
        List<Speaker> responseBody= (List<Speaker>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
        assertEquals(1L, responseBody.get(0).getSpeakerId());
        assertEquals(2L, responseBody.get(1).getSpeakerId());
    }
    @Test
    public void testGetAllSpeakers_NotFound(){
        when(speakerService.getAllSpeakers()).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = speakerController.getAllSpeakers();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Speakers don't found", response.getBody());
    }
//GET Speaker by ID
    @Test
    public void testGetSpeakerById_Success(){
    Long id= 3L;
    Speaker speaker = new Speaker();
    speaker.setSpeakerId(id);
    when(speakerService.getSpeakerById(id)).thenReturn(speaker);
    ResponseEntity<?> response = speakerController.getSpeakerById(id);
    Speaker resBody= (Speaker) response.getBody();
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(resBody);
    assertEquals(id, resBody.getSpeakerId());
    }
    @Test
    public void testGetSpeakerById_NotFound(){
        Long id=100L;
        when(speakerService.getSpeakerById(id)).thenThrow(new SpeakerNotFoundException(id));
        ResponseEntity<?> response = speakerController.getSpeakerById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Speaker with ID = " + id + " is not found", response.getBody());
    }
//GET Speaker by Name
    @Test
    public void testGetSpeakerByKeyword_Success(){
        Speaker speaker1 = new Speaker();
        speaker1.setLastName("Last name");
        Speaker speaker2= new Speaker();
        speaker2.setFirstName("First nAme");
        Speaker speaker3= new Speaker();
        speaker3.setCompany("My NAME");
        List<Speaker> speakers = Arrays.asList(speaker1, speaker2,speaker3);
        when(speakerService.getSpeakerByKeywordIgnoreCase("Name")).thenReturn(speakers);
        ResponseEntity<?> response = speakerController.getSpeakerByKeywordIgnoreCase("Name");
        List<Speaker> resBody= (List<Speaker>) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(resBody);
        assertEquals(3, resBody.size());
        assertTrue(resBody.get(0).getLastName().contains("name"));
        assertTrue(resBody.get(1).getFirstName().contains("nAme"));
        assertTrue(resBody.get(2).getCompany().contains("NAME"));
    }
    @Test
    public void testGetSpeakerByKeyword_NotFound(){
        when(speakerService.getSpeakerByKeywordIgnoreCase("Name")).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = speakerController.getSpeakerByKeywordIgnoreCase("Name");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Speakers don't found with keyword: Name", response.getBody());
    }
    @Test
    public void testGetSpeakerByKeyword_Exception(){
        when(speakerService.getSpeakerByKeywordIgnoreCase("any")).thenThrow(new RuntimeException("Server error"));
        ResponseEntity<?> response = speakerController.getSpeakerByKeywordIgnoreCase("any");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: in getByKeywordIgnoreCase", response.getBody());
    }
//Get Speaker by SessionId
    @Test
    public void testGetSpeakerBySessionId_Success(){
        List<Session> sessions = new ArrayList<>();
        Session session=new Session();
        session.setSessionId(1L);
        sessions.add(session);
        Speaker speaker= new Speaker();
        speaker.setSpeakerId(3L);
        speaker.setSessions(sessions);

        when(speakerService.getSpeakerBySessionId(1L)).thenReturn(Collections.singletonList(speaker));
        ResponseEntity<?> response = speakerController.getSpeakerBySessionId(1L);
        List<Speaker> resBody= (List<Speaker>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(resBody);
        assertEquals(3L, resBody.get(0).getSpeakerId());
    }
    @Test
    public void testGetSpeakerBySessionId_NotFound(){
        when(speakerService.getSpeakerBySessionId(1L)).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = speakerController.getSpeakerBySessionId(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Speaker don't found by Session ID: 1",response.getBody());
    }
    @Test
    public void testGetSpeakerBySessionId_Exception(){
        when(speakerService.getSpeakerBySessionId(anyLong())).thenThrow(new RuntimeException("Server error"));
        ResponseEntity<?> response = speakerController.getSpeakerBySessionId(200L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: in getSpeakerBySessionId", response.getBody());
    }
//Post Speaker
    @Test
    public void testPostSpeaker_Success(){
        Speaker speaker= new Speaker(35L,"First Name", "Last Name","title","Incorporation","Speaker Bio",  Collections.emptyList(), null);
        when(speakerService.postSpeaker(speaker)).thenReturn(speaker);
        ResponseEntity<?> response = speakerController.postSpeaker(speaker);
        Speaker resBody=(Speaker) response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(resBody);
        assertEquals(35, resBody.getSpeakerId());
        assertEquals("First Name", resBody.getFirstName());
        assertEquals("Last Name", resBody.getLastName());
        assertEquals("title", resBody.getTitle());
        assertEquals("Incorporation", resBody.getCompany());
        assertEquals("Speaker Bio", resBody.getSpeakerBio());
    }
    @Test void testPostSpeaker_BadRequest(){
        Speaker speaker= new Speaker();
        when(speakerService.postSpeaker(speaker)).thenThrow(new IllegalArgumentException());
        ResponseEntity<?> response = speakerController.postSpeaker(speaker);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ERROR: POST speaker: FirstName OR LastName Or Title OR Company Or speakerBio", response.getBody());
    }
//PUT Speaker
    @Test
    public void testPutSpeaker_Success(){
        Long id=35L;
        Speaker oldSpeaker=  new Speaker(id,"First Name", "Last Name","title","Incorporation","Speaker Bio",  Collections.emptyList(),null);
        Speaker newSpeaker= new Speaker(id,"First New", "Last New","title new","Incorporation New","New Bio",  Collections.emptyList(),null);

        when(speakerService.putSpeaker(id, oldSpeaker)).thenReturn(newSpeaker);
        ResponseEntity<?> response= speakerController.putSpeaker(id, oldSpeaker);
        Speaker resBody= (Speaker) response.getBody();
        assertNotNull(resBody);
        assertEquals(id, resBody.getSpeakerId());
        assertEquals("First New", resBody.getFirstName());
        assertEquals("Last New", resBody.getLastName());
        assertEquals("title new", resBody.getTitle());
        assertEquals("Incorporation New", resBody.getCompany());
        assertEquals("New Bio", resBody.getSpeakerBio());

}
    @Test
    public void testPutSpeaker_NotFound() throws SpeakerNotFoundException {
        Long id = 35L;
        Speaker speaker= new Speaker();
        speaker.setSpeakerId(id);
        when(speakerService.putSpeaker(anyLong(), any(Speaker.class))).thenThrow(new SpeakerNotFoundException(id));
        ResponseEntity<?> response = speakerController.putSpeaker(id, speaker);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Speaker with ID = " + id + " is not found",response.getBody());
    }
    @Test
    public void testPutSpeaker_Exception() throws SpeakerNotFoundException {
        Long id = 15L;
        Speaker speaker= new Speaker();
        speaker.setSpeakerId(id);
        when(speakerService.putSpeaker(anyLong(),any(Speaker.class))).thenThrow(new RuntimeException("Server error"));
        ResponseEntity<?> response = speakerController.putSpeaker(id, speaker);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: PUT Speaker: Server error", response.getBody());
    }
//PATCH Speaker
    @Test
    public void testPatchSpeaker_Success(){
        Long id = 35L;
        Speaker oldSpeaker= new Speaker();
        oldSpeaker.setSpeakerId(id);
        oldSpeaker.setFirstName("First Name");
        oldSpeaker.setLastName("Last Name");

        Speaker newSpeaker= new Speaker();
        newSpeaker.setSpeakerId(id);
        newSpeaker.setFirstName("First New");
        newSpeaker.setLastName("Last New");

        when(speakerService.patchSpeaker(id, oldSpeaker)).thenReturn(newSpeaker);
        ResponseEntity<?> response= speakerController.patchSpeaker(id, oldSpeaker);
        Speaker resBody= (Speaker) response.getBody();
        assertNotNull(resBody);
        assertEquals(id, resBody.getSpeakerId());
        assertEquals("First New", resBody.getFirstName());
        assertEquals("Last New", resBody.getLastName());
}
    @Test
    public void testPatchSpeaker_NotFound() throws SpeakerNotFoundException {
        Long id = 35L;
        Speaker speaker= new Speaker();
        speaker.setSpeakerId(id);
        when(speakerService.patchSpeaker(anyLong(), any(Speaker.class))).thenThrow(new SpeakerNotFoundException(id));
        ResponseEntity<?> response = speakerController.patchSpeaker(id, speaker);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Speaker with ID = " + id + " is not found",response.getBody());
    }
    @Test
    public void testPatchSpeaker_Exception() throws SpeakerNotFoundException {
        Long id = 15L;
        Speaker speaker= new Speaker();
        speaker.setSpeakerId(id);
        when(speakerService.patchSpeaker(anyLong(),any(Speaker.class))).thenThrow(new RuntimeException("Server error"));
        ResponseEntity<?> response = speakerController.patchSpeaker(id, speaker);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: PATCH Speaker: Server error", response.getBody());
    }
//DELETE Speaker
    @Test
    public void testDeleteSpeaker_Success(){
        Long id=39L;
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(id);
        doNothing().when(speakerService).deleteSpeaker(id);
        ResponseEntity<?> response= speakerController.deleteSpeaker(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Speaker with ID = " + id + " deleted", response.getBody());
}
    @Test
    public void testDeleteSpeaker_NotFound() throws SpeakerNotFoundException {
        Long id =40L;
        doThrow(new SpeakerNotFoundException(id)).when(speakerService).deleteSpeaker(id);
        ResponseEntity<?> response = speakerController.deleteSpeaker(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Speaker with ID = " + id + " is not found",response.getBody());
    }
    @Test
    public void testDeleteSpeaker_Exception() throws SpeakerNotFoundException {
        Long id = 15L;
        Speaker speaker= new Speaker();
        speaker.setSpeakerId(id);
        doThrow(new RuntimeException("Server error")).when(speakerService).deleteSpeaker(id);
        ResponseEntity<?> response = speakerController.deleteSpeaker(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR while DELETE speaker", response.getBody());
    }
}
