package com.doctorcare.PD_project.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "Doctor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Doctor extends User {
    String location;

    String specialization;

    String experience;

    long price;

    @Lob
    String description;

    double avgRating;

    int numberOfReviews;

    @OneToMany
    @JoinColumn(name = "appointment_id")
    List<Appointment> appointments;

    @OneToMany
    @JoinColumn(name = "schedule_id")
    List<Schedule> schedules;

    @OneToMany
    @JoinColumn(name = "review_id")
    List<Review> reviews;


}
