package com.liutyk.first_demo.services;

import com.liutyk.first_demo.models.SessionSchedule;
import com.liutyk.first_demo.repositories.SessionScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionScheduleServer {

    private final SessionScheduleRepository sessionScheduleRepository;

    @Autowired
    public SessionScheduleServer(SessionScheduleRepository sessionScheduleRepository) {
        this.sessionScheduleRepository = sessionScheduleRepository;
    }
    public List<SessionSchedule> getAllSessionSchedules() {
        return sessionScheduleRepository.findAll();
    }

    public List<SessionSchedule> findScheduleBySessionId(Long sessionId) {
        return sessionScheduleRepository.findScheduleBySessionId(sessionId);
    }

    public List<SessionSchedule> findByRoomIgnoreCase(String room) {
        return sessionScheduleRepository.findByRoomIgnoreCase(room);
    }

}
