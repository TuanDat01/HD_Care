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
import com.doctorcare.PD_project.service.SendEmailService;
import com.doctorcare.PD_project.service.VerifyTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/patient")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PatientController {
    PatientService patientService;
    VerifyTokenService verifyTokenService;
    SendEmailService emailService;
    @PostMapping
    public ApiResponse<Void> CreatePatient(@RequestBody @Valid CreateUserRequest userRequest, HttpServletRequest httpServletRequest) throws AppException {
        patientService.CreatePatient(userRequest,httpServletRequest);
        return ApiResponse.<Void>builder().build();
    }
    @PutMapping
    public ApiResponse<PatientRequest> updatePatient(@Valid @RequestBody PatientRequest patientRequest)
            throws AppException, IOException {

        return ApiResponse.<PatientRequest>builder()
                .result(patientService.updatePatient(patientRequest))
                .message("Update successful")
                .build();
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

    @PostMapping("/upload")
    public ApiResponse<List<String>> createUpload(@RequestParam("file") List<MultipartFile> fileList){
        List<String> list = emailService.sendImage(fileList);
        return ApiResponse.<List<String>>builder()
                .message("Upload successful")
                .result(list)
                .build();
    }


}
