package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.entity.MedicineDetail;
import com.doctorcare.PD_project.entity.Prescription;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.MedicineMapper;
import com.doctorcare.PD_project.responsitory.MedicineResponsitory;
import com.doctorcare.PD_project.responsitory.PrescriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class MedicineService {
    MedicineResponsitory medicineResponsitory;
    MedicineMapper medicineMapper;
    PrescriptionRepository prescriptionRepository;
    @Transactional
    public MedicineDetail CreateMedicine(MedicineDetail medicineDetail,String id) throws AppException {
        Prescription prescription = prescriptionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_MEDICINE));
        medicineDetail.setPrescription(prescription);
        System.out.println(medicineDetail);
        return medicineResponsitory.save(medicineDetail);
    }

    public MedicineDetail getMedicineById(String id) throws AppException {
        return medicineResponsitory.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_MEDICINE));
    }

    public void deleteMedicineById(String id) {
        medicineResponsitory.deleteById(id);
    }
    public MedicineDetail updateMedicine(String id, MedicineDetail updateMedicineDetail) throws AppException {
        MedicineDetail medicineDetail = getMedicineById(id);
        medicineMapper.updateMedicine(medicineDetail, updateMedicineDetail);
        return medicineResponsitory.save(medicineDetail);
    }
}
