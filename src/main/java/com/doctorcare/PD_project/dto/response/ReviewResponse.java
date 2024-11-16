package com.doctorcare.PD_project.dto.response;

import com.doctorcare.PD_project.entity.Review;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    List<Review> reviewList;
    double countAvg;
    int countReview;

    public void setCountAvg(List<Review> reviewList) {
        this.countAvg = sum(reviewList);
    }
    public void setCountReview(List<Review> reviewList) {
        this.countReview = reviewList.size();
    }
    public double sum(List<Review> reviewList){
        double total = 0;
        for (Review review : reviewList) {
            total += review.getRating();
        }
        return total/countReview;
    }
}
