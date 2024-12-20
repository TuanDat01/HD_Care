package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.dto.response.OtherDoctor;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Review;
import com.doctorcare.PD_project.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,String> {
    @Query("SELECT d From Doctor d where " +
            "(:city is null or d.city = :city) "+
            "AND (:district is null or d.district = :district) "+
            "AND (:name is null or d.name like %:name%)")
    Page<Doctor> filterDoctor(@Param("district") String district,
                              @Param("name") String name,
                              @Param("city") String city,
                              Pageable pageable);


    @Query("SELECT d from Doctor  d " +
            "join d.schedules s " +
            "where s.id = :scheduleId ")
    Doctor findDoctorBySchedules(@Param("scheduleId") String id);

    @Override
    long count();

    Optional<Doctor> findDoctorByUsername(String username);
    Optional<Doctor> findDoctorByEmail(String email);

    Optional<Doctor> findDoctorByPhone(String phone);

    @Query(value = "select d from Doctor d where d.id <> :doctorId and" +
            " (:city is null or d.city=:city) or" +
            " d.id <> :doctorId and (:name is null or d.name like %:name%) or" +
            " d.id <> :doctorId and (:price is null or d.price=:price)")
    Page<Doctor> findOtherDoctor(@Param("doctorId") String doctorId,
                                 @Param("city") String city,
                                      @Param("name") String name,
                                      @Param("price") long price,
                                 Pageable pageable);

//    @Query("SELECT r from Doctor d join d.reviews r where d.id = :doctorId ")
//    Page<Review> findReviewsByDoctorId(@Param("doctorId") String doctorId,
//                                       Pageable pageable);
}
