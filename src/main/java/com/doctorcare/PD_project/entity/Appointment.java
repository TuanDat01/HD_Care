package com.doctorcare.PD_project.entity;

import com.doctorcare.PD_project.enums.AppointmentStatus;
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
@Table(name = "Appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    String id;

    String title;



    @ManyToOne
    @JoinColumn(name = "doctor_id")
    Doctor doctor;


    @Column(name = "description")
    String des;

    @Column(name = "date")
    Date date;

    @Column(name = "status")
    AppointmentStatus status;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    Schedule schedule;


    @Column(name = "prescription")
    String prescript;

    @OneToOne
    @JoinColumn(name = "prescription_id")
    Prescription prescription;
}
