package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.enums.Roles;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import com.doctorcare.PD_project.responsitory.PatientRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class UserService{
    DoctorRepository doctorRepository;
    PatientRepository patientRepository;
    UserMapper userMapper;
    public UserResponse CreateDoctor(CreateUserRequest userRequest) {

        Doctor doctor = userMapper.toDoctor(userRequest);
        doctor.setRoles(Collections.singletonList(Roles.DOCTOR.name()));
        Doctor savedDoctor = doctorRepository.save(doctor);
        return userMapper.toUserResponse(savedDoctor);
    }

    public UserResponse CreatePatient(CreateUserRequest userRequest) {

        Patient patient = userMapper.toPatient(userRequest);
        patient.setRoles(Collections.singletonList(Roles.PATIENT.name()));
        Patient patientSaved = patientRepository.save(patient);
        return userMapper.toUserResponse(patientSaved);
    }
}
