package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReponsitory extends JpaRepository<Review,String> {
}
