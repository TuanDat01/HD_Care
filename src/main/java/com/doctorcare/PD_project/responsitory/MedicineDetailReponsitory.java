package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.entity.Medicine;
import com.doctorcare.PD_project.entity.MedicineDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineDetailReponsitory extends JpaRepository<MedicineDetail, String> {
}
