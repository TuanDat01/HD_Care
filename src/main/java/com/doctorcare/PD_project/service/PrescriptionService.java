package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.CreatePrescriptionRequest;
import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.MedicineDetail;
import com.doctorcare.PD_project.entity.Prescription;
import com.doctorcare.PD_project.enums.AppointmentStatus;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.responsitory.AppointmentRepository;
import com.doctorcare.PD_project.responsitory.MedicineResponsitory;
import com.doctorcare.PD_project.responsitory.PrescriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PrescriptionService {
    PrescriptionRepository prescriptionRepository;
    MedicineResponsitory medicineResponsitory;
    AppointmentRepository appointmentRepository;
    public CreatePrescriptionRequest createPrescription(Prescription prescription,String id) throws AppException {
        Prescription prescription1 = prescriptionRepository.findById(id)
                .orElseThrow();
        Appointment appointment = appointmentRepository.findAppointmentByPrescription(prescription1);

        if (Objects.equals(appointment.getStatus(), AppointmentStatus.PENDING.toString()))
            throw new AppException(ErrorCode.UPDATE_STATUS);

        prescription1.setResult(prescription.getResult());
        prescription1.setTimestamp(LocalDateTime.now());

        CreatePrescriptionRequest createPrescriptionRequest = new CreatePrescriptionRequest();
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


    public Prescription getPrescriptionById(String prescriptionId) throws AppException {

        return prescriptionRepository.findById(prescriptionId).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND_PRESCRIPTION));
    }
}
