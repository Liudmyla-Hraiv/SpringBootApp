package com.liutyk.first_demo.UnitServiceTests;

import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.repositories.SessionSpeakersRepository;
import com.liutyk.first_demo.repositories.SpeakerRepository;
import com.liutyk.first_demo.services.SpeakerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpeakerServiceTests {
    @Mock
    private SpeakerRepository speakerRepository;
    @Mock
    private SessionSpeakersRepository sessionSpeakersRepository;
    @InjectMocks
    private SpeakerService speakerService;

    private Long testSpeakerID=40L;
    private Long randomSpeaker= 17L;
    private Long randomSession= 39L;

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
        existingSpeaker.setSpeakerId(randomSpeaker);
        existingSpeaker.setFirstName("FirstName");
        existingSpeaker.setLastName("LastName");
        existingSpeaker.setTitle("Tittle");
        existingSpeaker.setCompany("Company");
        existingSpeaker.setSpeakerBio("Bio");
        when(speakerRepository.findById(randomSpeaker)).thenReturn(Optional.of(existingSpeaker));
        when(speakerRepository.saveAndFlush(existingSpeaker)).thenReturn(existingSpeaker);

        Speaker updatedSpeaker = new Speaker();
        updatedSpeaker.setFirstName("updatedFirstName");
        updatedSpeaker.setLastName("updatedLastName");
        updatedSpeaker.setTitle("updatedTittle");
        updatedSpeaker.setCompany("updatedCompany");
        updatedSpeaker.setSpeakerBio("updatedBio");


        Speaker result = speakerService.putSpeaker(randomSpeaker, updatedSpeaker);
        assertEquals(randomSpeaker, result.getSpeakerId());
        assertEquals("updatedFirstName", result.getFirstName());
        assertEquals("updatedLastName", result.getLastName());
        assertEquals("updatedTittle", result.getTitle());
        assertEquals("updatedCompany", result.getCompany());
        assertEquals("updatedBio", result.getSpeakerBio());

    }
    @Test
    public void testPatchSpeaker() {
        Speaker existingSpeaker = new Speaker();
        existingSpeaker.setSpeakerId(randomSpeaker);
        existingSpeaker.setFirstName("FirstName");
        existingSpeaker.setLastName("LastName");
        existingSpeaker.setTitle("Tittle");
        existingSpeaker.setCompany("Company");
        existingSpeaker.setSpeakerBio("Bio");
        when(speakerRepository.findById(randomSpeaker)).thenReturn(Optional.of(existingSpeaker));
        when(speakerRepository.saveAndFlush(existingSpeaker)).thenReturn(existingSpeaker);

        Speaker updatedSpeaker = new Speaker();
        updatedSpeaker.setFirstName("updatedFirstName");
        updatedSpeaker.setLastName("updatedLastName");
//        updatedSpeaker.setTitle("updatedTittle");
//        updatedSpeaker.setCompany("updatedCompany");
//        updatedSpeaker.setSpeakerBio("updatedBio");


        Speaker result = speakerService.patchSpeaker(randomSpeaker, updatedSpeaker);
        assertEquals(randomSpeaker, result.getSpeakerId());
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
