package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.DoctorRequest;
import com.doctorcare.PD_project.dto.request.UserRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.service.DoctorService;
import com.doctorcare.PD_project.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doctor")
@CrossOrigin(origins = "http://localhost:3000") // Chỉ cho phép localhost:3000 truy cập
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserService userService;
    DoctorService doctorService;

    @PostMapping("/create")
    public ApiResponse<UserResponse> CreateUser(@RequestBody UserRequest userRequest) {
        return userService.CreateUser(userRequest);
    }

    @PutMapping("/{id}")
    public ApiResponse<DoctorResponse> UpdateInfo(@PathVariable String id, @RequestBody DoctorRequest doctorRequest) {
        return ApiResponse.<DoctorResponse>builder().result(doctorService.UpdateInfo(id, doctorRequest)).build();
    }

    @GetMapping("/filter/{province}")
    public ApiResponse<List<DoctorResponse>> FilterByProvince(@PathVariable String province, @RequestParam(name = "name", required = false) String name) {
        List<DoctorResponse> doctorResponses = doctorService.FilterByProvince(province);
        if (name != null) {
            List<DoctorResponse> doctorResponseList = doctorResponses.stream()
                    .filter(doctor -> doctor.getName().contains(name)).toList();
            return ApiResponse.<List<DoctorResponse>>builder().result(doctorResponseList).build();
        } else
            return ApiResponse.<List<DoctorResponse>>builder().result(doctorResponses).build();
    }

    @GetMapping("/filter/")
    public ApiResponse<List<DoctorResponse>> FilterByProvince(@RequestParam(name = "name", required = false) String name) {
        return ApiResponse.<List<DoctorResponse>>builder().result(doctorService.FilterByName(name)).build();
    }

    @GetMapping("/price/asc")
    public ApiResponse<List<DoctorResponse>> OrderPriceAsc() {
        return ApiResponse.<List<DoctorResponse>>builder().result(doctorService.OrderPriceAsc()).build();
    }

    @GetMapping("/price/desc")
    public ApiResponse<List<DoctorResponse>> OrderPriceDesc() {
        return ApiResponse.<List<DoctorResponse>>builder().result(doctorService.OrderPriceDesc()).build();
    }
}