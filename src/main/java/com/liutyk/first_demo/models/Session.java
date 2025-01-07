package com.liutyk.first_demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    //@NotBlank is impossible to add because PATCH started to use it like mandatory fields
    //Verification added in SessionService
    @Size(min = 3, max = 80, message = "Session name must be between {min} and {max} characters")
    @Column(name = "session_name", nullable = false, length = 80)
    private String sessionName;

    @Size(min = 5, max = 1024, message = "Session description must be between {min} and {max} characters")
    @Column(name = "session_description", nullable = true, length = 1024)
    private String sessionDescription;

    @Positive(message = "Session length must be a positive number")
    @Column(name = "session_length", nullable = false)
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
