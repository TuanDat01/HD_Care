package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface ReviewRepository extends JpaRepository<Review,String> {
    @Query(value = "SELECT * FROM review r WHERE r.doctor_id = :doctorId " +
            "AND (:start IS NULL OR FLOOR(r.rating) = :start)", nativeQuery = true)

    Page<Review> findReviewsByDoctorId(@Param("doctorId") String doctorId,
                                       @Param("start") Double start,
                                       Pageable pageable);

    List<Review> findReviewByDoctorId(String id);

    @Query("SELECT floor (r.rating) AS star, COUNT(r) AS count " +
            "FROM Review r " +
            "GROUP BY floor (r.rating) " +
            "ORDER BY floor (r.rating) DESC")
    List<Object[]> countRating();
}
