package com.liutyk.first_demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Entity(name = "speakers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Speaker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "speaker_id")
    private  Long speakerId;

    @Size(min = 3, max =30)
    @Column(name = "first_name")
    private String firstName;

    @Size(min = 3, max = 30)
    @Column(name = "last_name")
    private String lastName;

    @Size(min = 3, max = 40)
    @Column(name = "title")
    private String title;

    @Column(name = "company")
    @Size(max = 50)
    private String company;

    @Column(name = "speaker_bio")
    @Size(max = 2000)
    private String speakerBio;


    @JsonIgnore
    @Size(max = 3145728)
    @Column (name = "speaker_photo", columnDefinition="BLOB")
    private byte[] speakerPhoto;

    @JsonIgnore
    @ManyToMany(mappedBy = "speakers", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Session> sessions;



    public Speaker(){
    }

    public Speaker(Long speakerId, String firstName, String lastName, String title, String company, String speakerBio, List<Session> sessions, byte[] speakerPhoto) {
        this.speakerId = speakerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.company = company;
        this.speakerBio = speakerBio;
        this.sessions = sessions;
        this.speakerPhoto = speakerPhoto;
    }


}
