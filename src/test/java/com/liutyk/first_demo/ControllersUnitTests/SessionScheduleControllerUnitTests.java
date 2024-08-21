package com.liutyk.first_demo.ControllersUnitTests;

import com.liutyk.first_demo.controllers.SessionScheduleController;
import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.models.SessionSchedule;
import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.services.SessionScheduleServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionScheduleControllerUnitTests {
    @Mock
    private SessionScheduleServer sessionScheduleServer;
    @InjectMocks
    private SessionScheduleController sessionScheduleController;
//GET ALL
    @Test
    public void testGetAllSessionSchedules_Success(){
        SessionSchedule schedule=new SessionSchedule();
        List<SessionSchedule> schedules= List.of(schedule);
        when(sessionScheduleServer.getAllSessionSchedules()).thenReturn(schedules);
        ResponseEntity<?> response = sessionScheduleController.getAllSessionSchedule();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    public void testGetAllSessionSchedules_NotFound(){
        Long id=200L;
        when(sessionScheduleServer.getAllSessionSchedules()).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = sessionScheduleController.getAllSessionSchedule();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Schedules Not Found" ,response.getBody());
    }
    @Test
    public void testGetAllSessionSchedules_Exception(){
        when(sessionScheduleServer.getAllSessionSchedules()).thenThrow(new RuntimeException("Server Error"));
        ResponseEntity<?> response= sessionScheduleController.getAllSessionSchedule();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: GET ALL schedules: Server Error", response.getBody());
    }
    //GET by Session ID
    @Test
    public void testGetScheduleBySessionId_Success(){
        SessionSchedule schedule = new SessionSchedule();
        schedule.setScheduleId(22L);
        schedule.setSession(new Session(1L, "", "", 65, Collections.singletonList(new Speaker()), Collections.emptyList()));
        List<SessionSchedule> schedules = List.of(schedule);

        when(sessionScheduleServer.getScheduleBySessionId(1L)).thenReturn(schedules);
        ResponseEntity<?> response = sessionScheduleController.getScheduleBySessionId(1L);
        List<SessionSchedule> resBody= (List<SessionSchedule>) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(resBody);
        assertEquals(22L, resBody.get(0).getScheduleId());
        assertEquals(1L, resBody.get(0).getSession().getSessionId());
    }
    @Test
    public void testGetScheduleBySessionId_NotFound(){
        Long id=200L;
        when(sessionScheduleServer.getScheduleBySessionId(id)).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = sessionScheduleController.getScheduleBySessionId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Schedule NOT FOUND by Session ID = " + id ,response.getBody());
    }
    @Test
    public void testGetScheduleBySessionId_Exception(){
        Long id=200L;
        when(sessionScheduleServer.getScheduleBySessionId(id)).thenThrow(new RuntimeException("Server Error"));
        ResponseEntity<?> response= sessionScheduleController.getScheduleBySessionId(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: GET Schedule by Session ID: Server Error", response.getBody());
    }
//Get by ROOM
    @Test
    public void testGetByRoomIgnoreCase_Success(){
        SessionSchedule schedule1 = new SessionSchedule();
        schedule1.setRoom("Team 1");
        SessionSchedule schedule2 = new SessionSchedule();
        schedule2.setRoom("tEAM 2");
        SessionSchedule schedule3 = new SessionSchedule();
        schedule3.setRoom("TEAM 3");
        List<SessionSchedule> schedules = Arrays.asList(schedule1, schedule2,schedule3);

        when(sessionScheduleServer.getByRoomIgnoreCase("team")).thenReturn(schedules);
        ResponseEntity<?> response = sessionScheduleController.getByRoomIgnoreCase("team");
        List<SessionSchedule> resBody= (List<SessionSchedule>) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(resBody);
        assertEquals(3, resBody.size());
        assertTrue(resBody.get(0).getRoom().contains("Team"));
        assertTrue(resBody.get(1).getRoom().contains("tEAM"));
        assertTrue(resBody.get(2).getRoom().contains("TEAM"));
    }
    @Test
    public void testGetByRoomIgnoreCase_NotFound(){
        String room="name";
        when(sessionScheduleServer.getByRoomIgnoreCase(room)).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = sessionScheduleController.getByRoomIgnoreCase(room);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Schedule by Room NOT FOUND: " + room,response.getBody());
    }
    @Test
    public void testGetByRoomIgnoreCase_Exception(){
        when(sessionScheduleServer.getByRoomIgnoreCase("name")).thenThrow(new RuntimeException("Server Error"));
        ResponseEntity<?> response= sessionScheduleController.getByRoomIgnoreCase("name");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR: GET schedule by ROOM Name: Server Error", response.getBody());
    }
}
