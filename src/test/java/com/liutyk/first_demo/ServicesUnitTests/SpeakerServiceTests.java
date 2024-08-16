package com.liutyk.first_demo.ServicesUnitTests;

import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.repositories.SessionRepository;
import com.liutyk.first_demo.repositories.SessionSpeakersRepository;
import com.liutyk.first_demo.repositories.SpeakerRepository;
import com.liutyk.first_demo.services.SpeakerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpeakerServiceTests {
    @Mock
    private SpeakerRepository speakerRepository;
    @Mock
    private SessionSpeakersRepository sessionSpeakersRepository;
    @Mock
    private SessionRepository sessionRepository;
    @InjectMocks
    private SpeakerService speakerService;

     Long testSpeakerID=40L;
     Long randomSpeakerId = 17L;
     Long randomSessionId = 39L;

    @Test
    public void testGetAllSpeakers(){
        List<Speaker> speakers= new ArrayList<>();
        speakers.add(new Speaker());
        speakers.add(new Speaker());
        when(speakerRepository.findAll()).thenReturn(speakers);
        List<Speaker> result= speakerService.getAllSpeakers();
        assertNotNull(result, "The result should not be null");
        assertFalse(result.isEmpty(), "The result list should not be empty");
        assertTrue(result.size()>=2, "The result list should contain at least two speakers");
    }
    @Test
    public void testGetSpeakerById(){
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(randomSpeakerId);
        speaker.setFirstName("fName");
        speaker.setLastName("lName");

        when(speakerRepository.findById(randomSpeakerId)).thenReturn(Optional.of(speaker));
        Optional<Speaker> result = speakerService.getSpeakerById(randomSpeakerId);
        assertTrue(result.isPresent(), "The result should be present" );
        assertEquals(randomSpeakerId, result.get().getSpeakerId());
        assertEquals("fName", result.get().getFirstName());
        assertEquals("lName", result.get().getLastName());
    }
    @Test
    public void testGetByKeywordIgnoreCase(){
        List<Speaker> speakers = new ArrayList<>();
        Speaker sp1 = new Speaker();
        sp1.setFirstName("Java Name");
        speakers.add(sp1);
        Speaker sp2= new Speaker();
        sp2.setLastName(" Class jAVA");
        speakers.add(sp2);
        Speaker sp3= new Speaker();
        sp3.setCompany("JAVA Inc.");
        speakers.add(sp3);

        when(speakerRepository.getByKeywordIgnoreCase("java")).thenReturn(speakers);
        List<Speaker> result= speakerService.getByKeywordIgnoreCase("java");
        assertFalse(result.isEmpty(), "The result should not be empty");
        assertTrue(result.size()>=3, "The result List should be at least three");
        assertTrue(result.get(0).getFirstName().contains("Java"), "The speaker FN should contain 'Java'");
        assertTrue(result.get(1).getLastName().contains("jAVA"), "The speaker LN should contain 'Java'");
        assertTrue(result.get(2).getCompany().contains("JAVA"), "The speaker company should contain 'Java'");
    }
    @Test
    public void testGetBySessionId(){
       List<Speaker> speakers =new ArrayList<>();
       Speaker speaker= new Speaker();
       speaker.setSpeakerId(randomSpeakerId);
       speakers.add(speaker);
       Session session = new Session();
       session.setSessionId(randomSessionId);
       session.setSpeakers(Collections.singletonList(speaker));

       when(speakerRepository.getBySessionId(randomSessionId)).thenReturn(speakers);
       List<Speaker> result= speakerService.getBySessionId(randomSessionId);
       assertFalse(result.isEmpty(), "The result should not be empty");
       assertEquals(result.get(0).getSpeakerId(), randomSpeakerId);
    }
    @Test
    public void testPostSpeaker(){
        Speaker savedSpeaker= new Speaker();
        savedSpeaker.setFirstName("testName");
        savedSpeaker.setLastName("testLastName");
        savedSpeaker.setTitle("testTittle");
        savedSpeaker.setCompany("testCompany");
        savedSpeaker.setSpeakerBio("testBio");

        when(speakerRepository.saveAndFlush(savedSpeaker)).thenReturn(savedSpeaker);

        Speaker resultSpeaker = speakerService.postSpeaker(savedSpeaker);
        assertEquals("testName", resultSpeaker.getFirstName());
        assertEquals("testLastName", resultSpeaker.getLastName());
        assertEquals("testTittle", resultSpeaker.getTitle());
        assertEquals("testCompany", resultSpeaker.getCompany());
        assertEquals("testBio", resultSpeaker.getSpeakerBio());
    }
//TODO: work with SpeakerPhoto

    @Test
    public void testPutSpeaker() {
        Speaker existingSpeaker = new Speaker();
        existingSpeaker.setSpeakerId(randomSpeakerId);
        existingSpeaker.setFirstName("FirstName");
        existingSpeaker.setLastName("LastName");
        existingSpeaker.setTitle("Tittle");
        existingSpeaker.setCompany("Company");
        existingSpeaker.setSpeakerBio("Bio");
        when(speakerRepository.findById(randomSpeakerId)).thenReturn(Optional.of(existingSpeaker));
        when(speakerRepository.saveAndFlush(existingSpeaker)).thenReturn(existingSpeaker);

        Speaker updatedSpeaker = new Speaker();
        updatedSpeaker.setFirstName("updatedFirstName");
        updatedSpeaker.setLastName("updatedLastName");
        updatedSpeaker.setTitle("updatedTittle");
        updatedSpeaker.setCompany("updatedCompany");
        updatedSpeaker.setSpeakerBio("updatedBio");


        Speaker result = speakerService.putSpeaker(randomSpeakerId, updatedSpeaker);
        assertEquals(randomSpeakerId, result.getSpeakerId());
        assertEquals("updatedFirstName", result.getFirstName());
        assertEquals("updatedLastName", result.getLastName());
        assertEquals("updatedTittle", result.getTitle());
        assertEquals("updatedCompany", result.getCompany());
        assertEquals("updatedBio", result.getSpeakerBio());

    }
    @Test
    public void testPatchSpeaker() {
        Speaker existingSpeaker = new Speaker();
        existingSpeaker.setSpeakerId(randomSpeakerId);
        existingSpeaker.setFirstName("FirstName");
        existingSpeaker.setLastName("LastName");
        existingSpeaker.setTitle("Tittle");
        existingSpeaker.setCompany("Company");
        existingSpeaker.setSpeakerBio("Bio");
        when(speakerRepository.findById(randomSpeakerId)).thenReturn(Optional.of(existingSpeaker));
        when(speakerRepository.saveAndFlush(existingSpeaker)).thenReturn(existingSpeaker);

        Speaker updatedSpeaker = new Speaker();
        updatedSpeaker.setFirstName("updatedFirstName");
        updatedSpeaker.setLastName("updatedLastName");
//        updatedSpeaker.setTitle("updatedTittle");
//        updatedSpeaker.setCompany("updatedCompany");
//        updatedSpeaker.setSpeakerBio("updatedBio");


        Speaker result = speakerService.patchSpeaker(randomSpeakerId, updatedSpeaker);
        assertEquals(randomSpeakerId, result.getSpeakerId());
        assertEquals("updatedFirstName", result.getFirstName());
        assertEquals("updatedLastName", result.getLastName());
        assertEquals("Tittle", result.getTitle());
        assertEquals("Company", result.getCompany());
        assertEquals("Bio", result.getSpeakerBio());
    }
    @Test
    public void testDeleteById() {
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(testSpeakerID);


        when(speakerRepository.existsById(testSpeakerID)).thenReturn(true);
        doNothing().when(sessionSpeakersRepository).deleteBySpeakerId(testSpeakerID);
        doNothing().when(speakerRepository).deleteById(testSpeakerID);

        speakerService.deleteSpeaker(testSpeakerID);

        verify(sessionSpeakersRepository).deleteBySpeakerId(testSpeakerID);
        verify(speakerRepository).deleteById(testSpeakerID);
    }
}
