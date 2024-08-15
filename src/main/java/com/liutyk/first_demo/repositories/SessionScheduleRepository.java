package com.liutyk.first_demo.repositories;

import com.liutyk.first_demo.models.SessionSchedule;
import com.liutyk.first_demo.models.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SessionScheduleRepository extends JpaRepository<SessionSchedule, Long> {
    @Modifying
    @Transactional
    void deleteBySession_SessionId(Long sessionId);

    @Query("SELECT s FROM session_schedule s WHERE LOWER(s.room) LIKE LOWER(CONCAT('%', :room, '%'))")
    List<SessionSchedule> getByRoomIgnoreCase(@Param("room") String keyword);

    @Query(value = "SELECT * FROM session_schedule sch WHERE sch.session_id = :sessionId", nativeQuery = true)
    List<SessionSchedule> getScheduleBySessionId(@Param("sessionId") Long id);
}
