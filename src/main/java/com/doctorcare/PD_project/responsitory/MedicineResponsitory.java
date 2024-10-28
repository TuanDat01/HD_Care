package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.entity.MedicineDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineResponsitory extends JpaRepository<MedicineDetail, String> {
    List<MedicineDetail> findByPrescriptionId(String prescriptionId);
}
