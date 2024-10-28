package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.PatientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "http://localhost:3000") // Chỉ cho phép localhost:3000 truy cập
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PatientController {
    PatientService patientService;
    @PostMapping
    public ApiResponse<UserResponse> CreatePatient(@RequestBody CreateUserRequest userRequest) {
        return ApiResponse.<UserResponse>builder().result(patientService.CreatePatient(userRequest)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Patient> getPatientById(@PathVariable String id) throws AppException {
        return ApiResponse.<Patient>builder().result(patientService.getPatientById(id)).build();
    }

}
