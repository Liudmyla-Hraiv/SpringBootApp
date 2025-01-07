package com.liutyk.first_demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    //@NotBlank is impossible to add because PATCH started to use it like mandatory fields
    //Verification added in SpeakerService
    @Size(min = 2, max =30, message = "Speaker first_name must be between {min} and {max} characters")
    @Column(name = "first_name",nullable = false, length = 30)
    private String firstName;

    //@NotBlank is impossible to add because PATCH started to use it like mandatory fields
    //Verification added in SpeakerService
    @Size(min = 2, max = 30, message = "Speaker last_name must be between {min} and {max} characters")
    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Size(max = 40, message = "Speaker title must be maximum {max} characters")
    @Column(name = "title", nullable = true, length = 40)
    private String title;

    @Size(max = 50, message = "Speaker company name must be maximum {max} characters")
    @Column(name = "company", nullable = true, length = 50)
    private String company;


    @Size(max = 2000, message = "Speaker Bio must be maximum {max} characters")
    @Column(name = "speaker_bio", nullable = true, length = 2000)
    private String speakerBio;


    @JsonIgnore
    @Size(max = 3145728, message = "Speaker photo must be maximum {max} characters")
    @Column (name = "speaker_photo", columnDefinition="BLOB", nullable = true, length = 3145728)
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
