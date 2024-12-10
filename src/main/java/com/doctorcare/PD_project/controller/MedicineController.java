package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.entity.Medicine;
import com.doctorcare.PD_project.entity.MedicineDetail;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.MedicineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ApiResponse<Medicine> UpdateMedicine(@PathVariable String id, @RequestBody Medicine medicine) throws AppException {
        return ApiResponse.<Medicine>builder().result(medicineService.updateMedicine(id, medicine)).build();
    }
    @DeleteMapping("/{id}")
    public void DeleteMedicine(@PathVariable String id) {
        medicineService.deleteMedicineById(id);
    }
    @GetMapping("/{id}")
    public ApiResponse<Medicine> GetMedicineById(@PathVariable String id) throws AppException {
        return ApiResponse.<Medicine>builder().result(medicineService.getMedicineById(id)).build();
    }
    @GetMapping
    public ApiResponse<List<MedicineDetail>> GetAllMedicine() {
        return ApiResponse.<List<MedicineDetail>>builder().result(medicineService.findAll()).build();
    }
}
