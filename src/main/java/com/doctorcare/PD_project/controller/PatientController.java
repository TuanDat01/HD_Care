package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.CreatePasswordRequest;
import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.request.PatientRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.entity.VerifyToken;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.PatientService;
import com.doctorcare.PD_project.service.VerifyTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PatientController {
    PatientService patientService;
    VerifyTokenService verifyTokenService;
    @PostMapping
    public ApiResponse<Void> CreatePatient(@RequestBody @Valid CreateUserRequest userRequest, HttpServletRequest httpServletRequest) throws AppException {
        patientService.CreatePatient(userRequest,httpServletRequest);
        return ApiResponse.<Void>builder().build();
    }
    @GetMapping("/verify")
    public ApiResponse<User> verifyAccount(@RequestParam(value = "token") String token) throws AppException {
        if(token == null) {
            throw new RuntimeException("Token is null");
        }
        return ApiResponse.<User>builder().result(verifyTokenService.updateAndDelete(token)).build();
    }
    @PutMapping("/{id}")
    public ApiResponse<PatientRequest> updatePatient(@PathVariable String id, @RequestBody PatientRequest patientRequest) throws AppException {
        return ApiResponse.<PatientRequest>builder().result(patientService.updatePatient(id, patientRequest)).build();
    }
    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getPatientById() throws AppException {
        return ApiResponse.<UserResponse>builder().result(patientService.getPatient()).build();
    }
    @GetMapping("/{id}")
    public ApiResponse<Patient> getPatientById(@PathVariable String id) throws AppException {
        return ApiResponse.<Patient>builder().result(patientService.getPatientById(id)).build();
    }


    @PostMapping("/create-password")
    public ApiResponse<Void> createPassword(@RequestBody @Valid CreatePasswordRequest createPasswordRequest) throws AppException {
        patientService.createPassword(createPasswordRequest);
        return ApiResponse.<Void>builder().message("Create password successful for login").build();
    }

}
