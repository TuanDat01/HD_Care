package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,String> {
    @Query("SELECT d From Doctor d where " +
            "(:city is null or d.city = :city) "+
            "AND (:district is null or d.district = :district) "+
            "AND (:name is null or d.name like %:name%)"
    )
    Page<Doctor> filterDoctor(@Param("district") String district, @Param("name") String name, @Param("city") String city,Pageable pageable);

    Page<Doctor> findAll(Pageable pageable);

    @Query("SELECT d from Doctor  d " +
            "join d.schedules s " +
            "where s.id = :scheduleId ")
    Doctor findDoctorBySchedules(@Param("scheduleId") String id);





}
