package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.Patient;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByUsername(String username);

    @Query("select s from Appointment  a" +
            " join a.patient s" +
            " where a.doctor.username = :userName")
    List<Patient> getPatientByDoctor(@Param("userName") String name);

    Optional<Object> findByPhone(String phone);
}
