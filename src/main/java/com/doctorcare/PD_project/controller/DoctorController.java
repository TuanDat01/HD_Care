package com.doctorcare.PD_project.controller;

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

    @PostMapping("/create")
    public ApiResponse<UserResponse> CreateUser(@RequestBody CreateUserRequest userRequest) {
        return userService.CreateUser(userRequest);
    }

    @GetMapping("/{id}")
    public ApiResponse<DoctorResponse> FindDoctor(@PathVariable String id) {
        return ApiResponse.<DoctorResponse>builder().result(doctorService.FindDoctorById(id)).build();
    }
    @PutMapping("/{id}")
    public ApiResponse<DoctorResponse> UpdateInfo(@PathVariable String id, @RequestBody UpdateDoctorRequest doctorRequest) {
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


    @GetMapping("/all")
    public ApiResponse<List<DoctorResponse>> GetAll() {
        return ApiResponse.<List<DoctorResponse>>builder().result(doctorService.GetAll()).build();
    }
}