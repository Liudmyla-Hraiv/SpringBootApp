package com.liutyk.first_demo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@Entity(name = "time_slots")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_slot_id")
    private Long timeSlotId;

    @NotNull
    @Column(name = "time_slot_date", nullable = false)
    private LocalDate timeSlotDate;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time",nullable = false)
    private LocalTime endTime;

    @NotNull
    @Column(name = "is_keynote_time_slot",nullable = false)
    private Boolean isKeynoteTimeSlot;

    public TimeSlot(){
    }
}
