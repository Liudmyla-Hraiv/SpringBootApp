package com.liutyk.first_demo.repositories;

import com.liutyk.first_demo.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
 @Query("SELECT s FROM sessions s WHERE LOWER(s.sessionName) LIKE LOWER(CONCAT('%', :sessionName, '%'))")
 List<Session> findBySessionNameContainingIgnoreCase(@Param("sessionName") String sessionName);


 @Query(value = "SELECT * FROM sessions s WHERE s.session_id IN " +
            "(SELECT sps.session_id FROM session_speakers sps WHERE sps.speaker_id = :speakerId)", nativeQuery = true)
 List<Session> findSessionsBySpeakerId(@Param("speakerId")Long speakerId);
}
