package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, String> {
    List<Medicine> findByPrescriptionId(String prescriptionId);
}
