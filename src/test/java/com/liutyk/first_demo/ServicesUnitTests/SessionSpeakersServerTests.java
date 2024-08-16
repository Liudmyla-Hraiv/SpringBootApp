package com.liutyk.first_demo.ServicesUnitTests;

import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.models.SessionSpeaker;
import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.repositories.SessionRepository;
import com.liutyk.first_demo.repositories.SessionSpeakersRepository;
import com.liutyk.first_demo.repositories.SpeakerRepository;
import com.liutyk.first_demo.services.SessionSpeakersServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionSpeakersServerTests {

    @Mock
    private SessionSpeakersRepository sessionSpeakersRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private SpeakerRepository speakerRepository;
    Long SpeakerId = 29L;
    Long SessionId1=21L;
    Long SessionId2=63L;

    @InjectMocks
    private SessionSpeakersServer sessionSpeakersServer;
    @Test
    public void testGetAllSessionSpeaker(){
        List<SessionSpeaker> sessionSpeakers = new ArrayList<>();
        sessionSpeakers.add(new SessionSpeaker());
        sessionSpeakers.add(new SessionSpeaker());
        sessionSpeakers.add(new SessionSpeaker());
        when(sessionSpeakersRepository.findAll()).thenReturn(sessionSpeakers);
        List<SessionSpeaker> results = sessionSpeakersServer.getAllSessionSpeakers();
        assertFalse(results.isEmpty(), "The results should not be empty");
        assertTrue(results.size()>=3, "The results should be more then 3 ");
    }
    @Test
    public void testGetBySessionID(){

        List<Speaker> speakers= new ArrayList<>();
        Speaker sp1 =new Speaker();
        sp1.setSpeakerId(SpeakerId);
        speakers.add(sp1);

        Session ses1 = new Session();
        ses1.setSessionId(SessionId1);
        ses1.setSpeakers(Collections.singletonList(sp1));

        List<SessionSpeaker> sessionSpeakers= new ArrayList<>();
        SessionSpeaker ssp1=new SessionSpeaker();
        ssp1.setSession(ses1);
        sessionSpeakers.add(ssp1);

        when(sessionSpeakersRepository.getBySessionId(SessionId1)).thenReturn(sessionSpeakers);
        List<SessionSpeaker> result = sessionSpeakersServer.getBySessionId(SessionId1);
        assertFalse(result.isEmpty(), "The results should not be empty");
        assertEquals(SessionId1, result.get(0).getSession().getSessionId());
    }

    @Test
    public void testGetBySpeakerID(){
        List<Speaker> speakers= new ArrayList<>();
        Speaker sp =new Speaker();
        sp.setSpeakerId(SpeakerId);
        speakers.add(sp);


        Session ses1 = new Session();
        ses1.setSessionId(SessionId1);
        ses1.setSpeakers(Collections.singletonList(sp));

        Session ses2 = new Session();
        ses2.setSessionId(SessionId2);
        ses2.setSpeakers(Collections.singletonList(sp));

        List<SessionSpeaker> sessionSpeakers= new ArrayList<>();
        SessionSpeaker ssp1=new SessionSpeaker();
        SessionSpeaker ssp2=new SessionSpeaker();
        ssp1.setSession(ses1);
        ssp2.setSession(ses2);
        sessionSpeakers.add(ssp1);
        sessionSpeakers.add(ssp2);

        when(sessionSpeakersRepository.getBySpeakerId(SpeakerId)).thenReturn(sessionSpeakers);
        List<SessionSpeaker> result = sessionSpeakersServer.getBySpeakerId(SpeakerId);
        assertFalse(result.isEmpty(), "The results should not be empty");
        assertEquals(SpeakerId, result.get(0).getSession().getSpeakers().get(0).getSpeakerId());
        assertEquals(SpeakerId, result.get(1).getSession().getSpeakers().get(0).getSpeakerId());

    }



}
