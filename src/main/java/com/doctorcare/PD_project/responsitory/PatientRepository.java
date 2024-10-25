package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
}
