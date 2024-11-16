package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, String> {

}
