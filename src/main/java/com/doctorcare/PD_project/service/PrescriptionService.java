package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.CreatePrescriptionRequest;
import com.doctorcare.PD_project.entity.MedicineDetail;
import com.doctorcare.PD_project.entity.Prescription;
import com.doctorcare.PD_project.responsitory.MedicineResponsitory;
import com.doctorcare.PD_project.responsitory.PrescriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PrescriptionService {
    PrescriptionRepository prescriptionRepository;
    MedicineResponsitory medicineResponsitory;
    public CreatePrescriptionRequest createPrescription(Prescription prescription,String id) {
        CreatePrescriptionRequest createPrescriptionRequest = new CreatePrescriptionRequest();
        Prescription prescription1 = prescriptionRepository.findById(id).orElseThrow();
        prescription1.setResult(prescription.getResult());
        prescription1.setTimestamp(LocalDate.now());
        createPrescriptionRequest.setResult(prescription1.getResult());
        createPrescriptionRequest.setTimestamp(prescription1.getTimestamp());
        List<MedicineDetail> medicineDetails = medicineResponsitory.findByPrescriptionId(id);
        createPrescriptionRequest.setMedicineDetails(medicineDetails);
        prescriptionRepository.save(prescription1);
        return createPrescriptionRequest;
    }
    public List<MedicineDetail> getMedicineByPrescription(String id) {
        return medicineResponsitory.findByPrescriptionId(id);
    }
}
