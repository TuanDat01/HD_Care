package com.doctorcare.PD_project.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String content;

    int rating;

    LocalDate date;

    @ElementCollection
    List<String> img;

    @OneToMany
    @JoinColumn(name = "like_review_id")
    List<LikeReview> likeReviews;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    Patient patient;

    @OneToMany
    @JoinColumn(name = "comment_id")
    List<Comment> comments;
    public void addImg(String image){
        if (img == null)
            img = new ArrayList<>();
        img.add(image);
    }




}
