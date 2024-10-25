package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findAllByPatientId(String patient_id);
}
