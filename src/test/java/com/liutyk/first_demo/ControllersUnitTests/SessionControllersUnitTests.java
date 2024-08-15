package com.liutyk.first_demo.ControllersUnitTests;

import com.liutyk.first_demo.controllers.SessionsController;
import com.liutyk.first_demo.models.Session;
import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class SessionControllersUnitTests {
    @Mock
    private SessionService sessionService;
    @InjectMocks
    private SessionsController sessionsController;

    private MockMvc mockMvc;
//Get all sessions
    @Test
    public void testGetAllSessions_NotEmptySessions() throws Exception{
        Speaker speaker = new Speaker();
        speaker.setSpeakerId(1L);
        List<Speaker> speakers = new ArrayList<>();
        speakers.add(speaker);
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
        mockMvc = MockMvcBuilders.standaloneSetup(sessionsController).build();
        mockMvc.perform(get("/api/v1/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sessionId").value(1L))
                .andExpect(jsonPath("$[0].sessionName").value("Session 1"))
                .andExpect(jsonPath("$[0].sessionDescription").value("Description for Session 1"))
                .andExpect(jsonPath("$[0].sessionLength").value(60))
                .andExpect(jsonPath("$[1].sessionId").value(2L))
                .andExpect(jsonPath("$[1].sessionName").value("Session 2"))
                .andExpect(jsonPath("$[1].sessionDescription").value("Description for Session 2"))
                .andExpect(jsonPath("$[1].sessionLength").value(90))
                .andExpect(jsonPath("$[1].speakers[0].speakerId").value(1L));
    }
    @Test
    public void testGetAllSessions_NoSessions() throws Exception{
        when(sessionService.getAllSessions()).thenReturn(Collections.emptyList());
        mockMvc = MockMvcBuilders.standaloneSetup(sessionsController).build();
        mockMvc.perform(get("/api/v1/sessions"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("There ara NO any information about session"));
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
        mockMvc = MockMvcBuilders.standaloneSetup(sessionsController).build();
        mockMvc.perform(get("/api/v1/sessions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(1L))
                .andExpect(jsonPath("$.sessionName").value("Session"))
                .andExpect(jsonPath("$.sessionDescription").value("Description for Session"))
                .andExpect(jsonPath("$.sessionLength").value(60));

    }
    @Test
    public void testGetSessionById_SessionAbsent() throws Exception{
        Session session = new Session();
        session.setSessionId(1L);
        session.setSessionName("Session");
        session.setSessionDescription("Description for Session");
        session.setSessionLength(60);

        when(sessionService.getSessionById(188L)).thenReturn(Optional.empty());
        mockMvc = MockMvcBuilders.standaloneSetup(sessionsController).build();
        mockMvc.perform(get("/api/v1/sessions/188"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Session Not found"));

    }
//


}
