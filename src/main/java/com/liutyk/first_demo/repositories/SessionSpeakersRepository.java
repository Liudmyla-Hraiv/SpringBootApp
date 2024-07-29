package com.liutyk.first_demo.repositories;

import com.liutyk.first_demo.models.Speaker;
import com.liutyk.first_demo.models.SessionSpeakers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionSpeakersRepository extends JpaRepository<SessionSpeakers, Speaker> {
    @Query(value = "SELECT * FROM session_speakers ssp WHERE ssp.session_id = :sessionId", nativeQuery = true)
    List<SessionSpeakers> findBySessionId(@Param("sessionId") Long id);

    @Query(value = "SELECT * FROM session_speakers ssp WHERE ssp.speaker_id = :speakerId", nativeQuery = true)
    List<SessionSpeakers> findBySpeakerId(@Param("speakerId") Long id);
}
