package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.CreateScheduleRequest;
import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.request.UpdateDoctorRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.service.DoctorService;
import com.doctorcare.PD_project.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@CrossOrigin(origins = "http://localhost:3000") // Chỉ cho phép localhost:3000 truy cập
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class DoctorController {
    UserService userService;
    DoctorService doctorService;

    @PostMapping
    public ApiResponse<UserResponse> CreateUser(@RequestBody CreateUserRequest userRequest) {
        return ApiResponse.<UserResponse>builder().result(userService.CreateDoctor(userRequest)).build();
    }
    @GetMapping("/{id}")
    public ApiResponse<DoctorResponse> FindDoctor(@PathVariable String id) {
        return ApiResponse.<DoctorResponse>builder().result(doctorService.FindDoctorById(id)).build();
    }
    @PutMapping("/{id}")
    public ApiResponse<DoctorResponse> UpdateInfo(@PathVariable String id, @RequestBody UpdateDoctorRequest doctorRequest) {
        return ApiResponse.<DoctorResponse>builder().result(doctorService.UpdateInfo(id, doctorRequest)).build();
    }


    @GetMapping
    public ApiResponse<List<DoctorResponse>> getAll(@RequestParam(name = "name", required = false) String name,@RequestParam(name = "district", required = false) String district,@RequestParam(name = "city",required = false) String city, @RequestParam(name = "page",required = false) String page) {
       return ApiResponse.<List<DoctorResponse>>builder().result(doctorService.GetAll(district, name, city,page)).build();
    }


}