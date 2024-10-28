package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.entity.MedicineDetail;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.MedicineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicine")
@CrossOrigin(origins = "http://localhost:3000") // Chỉ cho phép localhost:3000 truy cập
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class MedicineController {
    MedicineService medicineService;
//    @PostMapping
//    public ApiResponse<MedicineDetail> CreateMedicine(@RequestBody MedicineDetail medicineDetail) {
//        return ApiResponse.<MedicineDetail>builder().result(medicineService.CreateMedicine(medicineDetail)).build();
//    }
    @PutMapping("/{id}")
    public ApiResponse<MedicineDetail> UpdateMedicine(@PathVariable String id, @RequestBody MedicineDetail medicineDetail) throws AppException {
        return ApiResponse.<MedicineDetail>builder().result(medicineService.updateMedicine(id,medicineDetail)).build();
    }
    @DeleteMapping("/{id}")
    public void DeleteMedicine(@PathVariable String id) {
        medicineService.deleteMedicineById(id);
    }
    @GetMapping("/{id}")
    public ApiResponse<MedicineDetail> GetMedicineById(@PathVariable String id) throws AppException {
        return ApiResponse.<MedicineDetail>builder().result(medicineService.getMedicineById(id)).build();
    }
}
