package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.entity.Medicine;
import com.doctorcare.PD_project.entity.MedicineDetail;
import com.doctorcare.PD_project.entity.Prescription;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.MedicineMapper;
import com.doctorcare.PD_project.responsitory.MedicineDetailReponsitory;
import com.doctorcare.PD_project.responsitory.MedicineResponsitory;
import com.doctorcare.PD_project.responsitory.PrescriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class MedicineService {
    MedicineResponsitory medicineResponsitory;
    MedicineMapper medicineMapper;
    PrescriptionRepository prescriptionRepository;
    MedicineDetailReponsitory medicineDetailReponsitory;
    @Transactional
    public Medicine CreateMedicine(Medicine medicine, String id) throws AppException {
        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_MEDICINE));
        medicine.setPrescription(prescription);
        System.out.println(medicine);
        return medicineResponsitory.save(medicine);
    }

    public Medicine getMedicineById(String id) throws AppException {
        return medicineResponsitory.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_MEDICINE));
    }

    public void deleteMedicineById(String id) {
        medicineResponsitory.deleteById(id);
    }
    public Medicine updateMedicine(String id, Medicine updateMedicine) throws AppException {
        Medicine medicine = getMedicineById(id);
        medicineMapper.updateMedicine(medicine, updateMedicine);
        return medicineResponsitory.save(medicine);
    }

    public List<MedicineDetail> findAll() {
        return medicineDetailReponsitory.findAll();
    }
}
