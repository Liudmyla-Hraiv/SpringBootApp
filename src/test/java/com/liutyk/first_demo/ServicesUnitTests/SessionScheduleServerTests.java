package com.liutyk.first_demo.ServicesUnitTests;

import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.models.SessionSchedule;
import com.liutyk.first_demo.repositories.SessionRepository;
import com.liutyk.first_demo.repositories.SessionScheduleRepository;
import com.liutyk.first_demo.services.SessionScheduleServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionScheduleServerTests {
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private SessionScheduleRepository sessionScheduleRepository;

    @InjectMocks
    private SessionScheduleServer sessionScheduleServer;

    Long SessionID =4L;


    @Test
    public void testGetAllSessionSchedules(){
        List<SessionSchedule> sSchedules = new ArrayList<>();
        sSchedules.add(new SessionSchedule());
        sSchedules.add(new SessionSchedule());
        sSchedules.add(new SessionSchedule());
        when(sessionScheduleRepository.findAll()).thenReturn(sSchedules);
        List<SessionSchedule> results = sessionScheduleServer.getAllSessionSchedules();
        assertFalse(results.isEmpty(), "The results should not be empty");
        assertTrue(results.size()>=3, "The results should be more then 3 ");
    }
    @Test
    public void testGetScheduleBySessionId(){
        List<SessionSchedule> sSchedules = new ArrayList<>();
        SessionSchedule sSch= new SessionSchedule();
        Session ses = new Session();
        ses.setSessionId(SessionID);
        sSch.setSession(ses);
        sSchedules.add(sSch);

        when(sessionScheduleRepository.getScheduleBySessionId(SessionID)).thenReturn(sSchedules);

        List<SessionSchedule> result = sessionScheduleServer.getScheduleBySessionId(SessionID);
        assertFalse(result.isEmpty(), "The results should not be empty");
        assertEquals(SessionID, result.get(0).getSession().getSessionId());
    }

    @Test
    public void testGetScheduleByRoomIgnoreCase(){
        List<SessionSchedule> sSchedules = new ArrayList<>();
        SessionSchedule sch1= new SessionSchedule();
        sch1.setRoom("REST API");
        SessionSchedule sch2= new SessionSchedule();
        sch2.setRoom("SOUP Api");
        sSchedules.add(sch1);
        sSchedules.add(sch2);

        when(sessionScheduleRepository.getByRoomIgnoreCase("api")).thenReturn(sSchedules);
        List<SessionSchedule> results = sessionScheduleServer.getByRoomIgnoreCase("api");
        assertFalse(results.isEmpty(), "The results should not be empty");
        assertTrue(results.get(0).getRoom().contains("API"), "The sessionSchedule room should contain 'api'");
        assertTrue(results.get(1).getRoom().contains("Api"), "The sessionSchedule room should contain 'api'");
        assertTrue(results.size()>=2, "The results should be more then 2");

    }
}
