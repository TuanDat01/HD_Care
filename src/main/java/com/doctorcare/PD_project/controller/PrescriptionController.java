package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.CreatePrescriptionRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.entity.Medicine;
import com.doctorcare.PD_project.entity.Prescription;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.MedicineService;
import com.doctorcare.PD_project.service.PrescriptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescription")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PrescriptionController {
    MedicineService medicineService;
    PrescriptionService prescriptionService;
    @PostMapping("/{id}/medicine")
    public ApiResponse<Medicine> CreateMedicine(@PathVariable String id, @RequestBody Medicine medicine) throws AppException {
        return ApiResponse.<Medicine>builder().result(medicineService.CreateMedicine(medicine,id)).build();
    }
    @PutMapping("/{id}")
    public ApiResponse<CreatePrescriptionRequest> createPrescription(@RequestBody Prescription prescription, @PathVariable String id) throws AppException {
        return ApiResponse.<CreatePrescriptionRequest>builder().result(prescriptionService.createPrescription(prescription, id)).build();
    }
    @GetMapping("/{id}/medicine")
    public ApiResponse<List<Medicine>> getMedicineByPrescription(@PathVariable String id) throws AppException {
        return ApiResponse.<List<Medicine>>builder().result(prescriptionService.getMedicineByPrescription(id)).build();
    }

}
