package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.AppointmentResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.service.PatientService;
import com.doctorcare.PD_project.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "http://localhost:3000") // Chỉ cho phép localhost:3000 truy cập
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PatientController {
    UserService userService;
    PatientService patientService;
    @PostMapping
    public ApiResponse<UserResponse> CreatePatient(@RequestBody CreateUserRequest userRequest) {
        return ApiResponse.<UserResponse>builder().result(userService.CreatePatient(userRequest)).build();
    }

//
//    @GetMapping("/{id}/appointment")
//    public ApiResponse<List<AppointmentResponse>> findAppointment(@PathVariable String id){
//        return ApiResponse.<List<AppointmentResponse>>builder().result(patientService.findAppointment(id)).build();
//    }
}
