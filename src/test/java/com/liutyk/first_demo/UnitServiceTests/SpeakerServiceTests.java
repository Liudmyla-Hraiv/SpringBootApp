package com.liutyk.first_demo.UnitServiceTests;

import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.models.Speaker;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpeakerServiceTests {
    @Mock
    private SpeakerRepository speakerRepository;
    @InjectMocks
    private SpeakerService speakerService;

    public Long testSpeakerID1;
    public Long testSpeakerID2;

    @Test
    public void testCreateSpeaker(){
        Speaker savedSpeaker= new Speaker();
        savedSpeaker.setFirstName("testName");
        savedSpeaker.setLastName("testLastName");
        savedSpeaker.setTitle("testTittle");
        savedSpeaker.setCompany("testCompany");
        savedSpeaker.setSpeakerBio("testBio");

        when(speakerRepository.saveAndFlush(any(Speaker.class))).thenReturn(savedSpeaker);

        Speaker resultSpeaker = speakerService.createSpeaker(savedSpeaker);
        testSpeakerID1 =resultSpeaker.getSpeakerId();
        assertEquals("testName", resultSpeaker.getFirstName());
        assertEquals("testLastName", resultSpeaker.getLastName());
        assertEquals("testTittle", resultSpeaker.getTitle());
        assertEquals("testCompany", resultSpeaker.getCompany());
        assertEquals("testBio", resultSpeaker.getSpeakerBio());
        verify(speakerRepository).saveAndFlush(savedSpeaker);

    }
//TODO: work with SpeakerPhoto
@Test
public void testCreateSpeaker2(){
    Speaker savedSpeaker2= new Speaker();
    savedSpeaker2.setFirstName("testName2");
    savedSpeaker2.setLastName("testLastName2");
    savedSpeaker2.setTitle("testTittle2");
    savedSpeaker2.setCompany("testCompany2");
    savedSpeaker2.setSpeakerBio("testBio2");

    when(speakerRepository.saveAndFlush(any(Speaker.class))).thenReturn(savedSpeaker2);

    Speaker resultSpeaker = speakerService.createSpeaker(savedSpeaker2);
    testSpeakerID2 =resultSpeaker.getSpeakerId();
    assertEquals("testName2", resultSpeaker.getFirstName());
    assertEquals("testLastName2", resultSpeaker.getLastName());
    assertEquals("testTittle2", resultSpeaker.getTitle());
    assertEquals("testCompany2", resultSpeaker.getCompany());
    assertEquals("testBio2", resultSpeaker.getSpeakerBio());
    verify(speakerRepository).saveAndFlush(savedSpeaker2);

}
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
    public void testUpdateSpeaker() {
        Speaker existingSpeaker = new Speaker();
        existingSpeaker.setSpeakerId(testSpeakerID2);
        when(speakerRepository.findById(testSpeakerID2)).thenReturn(Optional.of(existingSpeaker));
        when(speakerRepository.saveAndFlush(existingSpeaker)).thenReturn(existingSpeaker);

        Speaker updatedSpeaker = new Speaker();
        updatedSpeaker.setFirstName("updatedFirstName");
        updatedSpeaker.setLastName("updatedLastName");
        updatedSpeaker.setTitle("updatedTittle");
        updatedSpeaker.setCompany("updatedCompany");
        updatedSpeaker.setSpeakerBio("updatedBio");


        Speaker result = speakerService.updateSpeaker(testSpeakerID2, updatedSpeaker);
        assertEquals("updatedFirstName", result.getFirstName());
        assertEquals("updatedLastName", result.getLastName());
        assertEquals("updatedTittle", result.getTitle());
        assertEquals("updatedCompany", result.getCompany());
        assertEquals("updatedBio", result.getSpeakerBio());
    }

//    @Test
//    public void testDeleteById() {
//        Speaker speaker = new Speaker();
//        speaker.setSpeakerId(testSpeakerID1);
//        when(speakerRepository.findById(testSpeakerID1)).thenReturn(Optional.of(speaker));
//
//
//        speakerService.deleteSpeaker(testSpeakerID1);
//        verify(speakerRepository).deleteById(testSpeakerID1);
//
//    }
}
