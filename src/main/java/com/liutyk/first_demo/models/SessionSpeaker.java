package com.liutyk.first_demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "session_speakers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SessionSpeaker {
    @EmbeddedId
    private SessionSpeakerKey id;


    @ManyToOne
    @MapsId("sessionId")
    @JoinColumn(name = "session_id")
    private Session session;

    @JsonIgnore
    @ManyToOne
    @MapsId("speakerId")
    @JoinColumn(name = "speaker_id")
    private Speaker speaker;
    public SessionSpeaker(){
    }

    public SessionSpeaker(Session session, Speaker speaker) {
        this.session = session;
        this.speaker = speaker;
    }
}

