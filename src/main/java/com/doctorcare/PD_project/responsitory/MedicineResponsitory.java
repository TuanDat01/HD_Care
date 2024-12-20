package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineResponsitory extends JpaRepository<Medicine, String> {
    List<Medicine> findByPrescriptionId(String prescriptionId);
}
