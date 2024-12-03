package com.doctorcare.PD_project.entity;

import com.doctorcare.PD_project.enums.AppointmentStatus;
import com.doctorcare.PD_project.event.listen.EntityListener;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Appointment")
@EntityListeners(EntityListener.class)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "appointment_id")
    String id;
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    String status;
    @Column(name = "noteStatus")
    String note;

    @ManyToOne
    @JoinColumn(name = "doctor_id", unique = true, nullable = false)
    Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "patient_id", unique = true, nullable = false)
    Patient patient;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    Schedule schedule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prescription_id")
    Prescription prescription;

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", prescription=" + prescription +
                '}';
    }
}
