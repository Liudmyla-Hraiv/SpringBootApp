package com.liutyk.first_demo.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name= "session_schedule")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SessionSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "time_slot_id", nullable = false)
    private TimeSlot timeSlot;

    @Size(min =1, max=30)
    @Column(name = "room", nullable = false)
    private String room;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    public SessionSchedule(){
    }


}
