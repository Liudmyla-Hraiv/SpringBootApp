package com.liutyk.first_demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class SessionSpeakersKey implements Serializable {
    @Column(name = "speaker_id")
    private Long speakerId;

    @Column(name = "session_id")
    private Long sessionId;

    public SessionSpeakersKey(){
    }
    public SessionSpeakersKey(Long speakerId, Long sessionId){
        this.speakerId=speakerId;
        this.sessionId=sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionSpeakersKey that = (SessionSpeakersKey) o;
        return Objects.equals(speakerId, that.speakerId) &&
                Objects.equals(sessionId, that.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(speakerId, sessionId);
    }
}
