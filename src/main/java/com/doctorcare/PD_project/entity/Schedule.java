package com.doctorcare.PD_project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    @Column(name = "start_time")
    Date start;

    @Column(name = "end_time")
    Date end;

    @Column(name = "is_available")
    boolean available;



    @OneToMany
    @JoinColumn(name = "appointment_id")
    List<Appointment> appointments;
}
