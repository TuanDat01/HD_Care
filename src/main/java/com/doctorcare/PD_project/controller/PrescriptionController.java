package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.CreatePrescriptionRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.entity.MedicineDetail;
import com.doctorcare.PD_project.entity.Prescription;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.MedicineService;
import com.doctorcare.PD_project.service.PatientService;
import com.doctorcare.PD_project.service.PrescriptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescription")
@CrossOrigin(origins = "http://localhost:3000") // Chỉ cho phép localhost:3000 truy cập
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PrescriptionController {
    MedicineService medicineService;
    PrescriptionService prescriptionService;
    @PostMapping("/{id}/medicine")
    public ApiResponse<MedicineDetail> CreateMedicine(@PathVariable String id, @RequestBody MedicineDetail medicineDetail) throws AppException {
        return ApiResponse.<MedicineDetail>builder().result(medicineService.CreateMedicine(medicineDetail,id)).build();
    }
    @PutMapping("/{id}")
    public ApiResponse<CreatePrescriptionRequest> createPrescription(@RequestBody Prescription prescription, @PathVariable String id) {
        return ApiResponse.<CreatePrescriptionRequest>builder().result(prescriptionService.createPrescription(prescription, id)).build();
    }
    @GetMapping("/{id}/medicine")
    public ApiResponse<List<MedicineDetail>> getMedicineByPrescription(@PathVariable String id) throws AppException {
        return ApiResponse.<List<MedicineDetail>>builder().result(prescriptionService.getMedicineByPrescription(id)).build();
    }
}
