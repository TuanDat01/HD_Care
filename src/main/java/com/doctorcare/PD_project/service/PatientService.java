package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.request.PatientRequest;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.enums.Roles;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.AppointmentMapper;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.PatientRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PatientService {
    PatientRepository patientRepository;
    UserMapper userMapper;
    public UserResponse CreatePatient(CreateUserRequest userRequest) {

        Patient patient = userMapper.toPatient(userRequest);
        patient.setRole(Roles.PATIENT.name());
        Patient patientSaved = patientRepository.save(patient);
        return userMapper.toUserResponse(patientSaved);
    }
    public Patient updatePatient(AppointmentRequest appointmentRequest) throws AppException {
        Patient patient = patientRepository.findById(appointmentRequest.getIdPatient()).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_PATIENT));
        System.out.println(patient);
        PatientRequest updatePatient = userMapper.toPatientRequest(appointmentRequest);
        userMapper.updatePatient(patient,updatePatient);
        System.out.println(patient);
        return patientRepository.save(patient);
    }

    public Patient getPatientById(String id) throws AppException {
        return patientRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_PATIENT));
    }
}
