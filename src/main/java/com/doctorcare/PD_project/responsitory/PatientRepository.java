package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.dto.response.PatientGetByAdminResponse;
import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.Patient;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("select new com.doctorcare.PD_project.dto.response.PatientGetByAdminResponse(" +
            "p.id, p.name, p.dob, p.username, p.phone, p.email, p.gender, p.img, p.address, p.enable) " +
            "from Patient p where " +
            "(:name is null or p.name like %:name%)")
    Page<PatientGetByAdminResponse> findAllByAdmin(Pageable pageable, @Param("name") String name);
}
