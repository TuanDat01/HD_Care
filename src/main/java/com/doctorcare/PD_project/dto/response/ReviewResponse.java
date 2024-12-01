package com.doctorcare.PD_project.dto.response;

import com.doctorcare.PD_project.entity.Review;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    List<Review> reviewList;
    double countAvg;
    long countReview;
    long pageMax;
    List<Object[]> countRating;


    public void setCountReview(long countReview) {
        this.countReview = countReview;
    }

}
