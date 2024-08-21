package com.liutyk.first_demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity(name = "sessions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @NotEmpty
    @Size(min = 3, max = 80)
    @Column(name = "session_name")
    private String sessionName;

    @Size(min = 1, max = 1024)
    @Column(name = "session_description")
    private String sessionDescription;

    @Positive
    @Column(name = "session_length" )
    private Integer sessionLength;

    public Session(){
    }
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "session_speakers",
            joinColumns = @JoinColumn (name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "speaker_id"))
    private List<Speaker> speakers;

    @JsonIgnore
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionSchedule> schedules = new ArrayList<>();




    public Session(Long sessionId, String sessionName, String sessionDescription, Integer sessionLength, List<Speaker> speakers, List<SessionSchedule> schedules) {
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.sessionDescription = sessionDescription;
        this.sessionLength = sessionLength;
        this.speakers = speakers;
        this.schedules = schedules;
    }

}
