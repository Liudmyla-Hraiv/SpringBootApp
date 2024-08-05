package com.liutyk.first_demo.repositories;

import com.liutyk.first_demo.models.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Long> {

    @Query(value = "SELECT * FROM speakers sp WHERE sp.speaker_id IN " +
            "(SELECT sps.speaker_id FROM session_speakers sps WHERE sps.session_id = :sessionId)", nativeQuery = true)
    List<Speaker> findBySessionId(@Param("sessionId") Long sessionId);


    @Query("SELECT s FROM speakers s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.company) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Speaker> findByKeywordIgnoreCase(@Param("keyword") String keyword);
}