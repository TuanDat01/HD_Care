package com.doctorcare.PD_project.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@DiscriminatorValue("Review")
public class LikeReview extends Like {

    @ManyToOne
    @JoinColumn(name = "patient_liked_id")
    Patient patientLiked;
}
