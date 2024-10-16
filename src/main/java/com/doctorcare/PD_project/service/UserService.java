package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.enums.Roles;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import com.doctorcare.PD_project.responsitory.UserResponsitory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class UserService{
    UserResponsitory userResponsitory;
    DoctorRepository doctorResponsitory;
    UserMapper userMapper;
    public ApiResponse<UserResponse> CreateUser(CreateUserRequest userRequest) {

        Doctor doctor = userMapper.toDoctor(userRequest);
        doctor.setRole(Roles.DOCTOR.name());
        Doctor savedDoctor = doctorResponsitory.save(doctor);
        return ApiResponse.<UserResponse>builder().result(userMapper.toUserResponse(savedDoctor)).build();
    }
}
