package com.doctorcare.PD_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Doctor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Doctor extends User {

    String district;
    String city;
    String specialization;
    @Lob
    String experience;
    long price;
    double avgRating;
    long numberOfReviews;
    String address;

    @Lob
    String description;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @JsonIgnore
    List<Schedule> schedules;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "doctor_id")
//    List<Review> reviews;

    public void addSchedule(Schedule schedule){
        if(schedules == null){
            schedules = new ArrayList<>();
        }
        schedules.add(schedule);
    }

//    public void addReview(Review review){
//        if(reviews == null){
//            reviews = new ArrayList<>();
//        }
//        reviews.add(review);
//    }
}
