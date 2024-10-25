package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.request.PatientRequest;
import com.doctorcare.PD_project.dto.response.AppointmentResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.enums.Roles;
import com.doctorcare.PD_project.mapping.AppointmentMapper;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import com.doctorcare.PD_project.responsitory.PatientRepository;
import com.doctorcare.PD_project.responsitory.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PatientService {
    PatientRepository patientRepository;
    AppointmentMapper appointmentMapper;
    UserMapper userMapper;
//    public List<AppointmentResponse> findAppointment(String id){
//        Patient patient = patientRepository.findById(id).orElseThrow(()->new RuntimeException("khong thay patient"));
//        List<Appointment> appointmentList = patient.getAppointments();
//        return appointmentList.stream().map((appointmentMapper::appointmentResponse)).toList();
//    }
    public Patient updatePatient(AppointmentRequest appointmentRequest){
        Patient patient = patientRepository.findById(appointmentRequest.getIdPatient()).orElseThrow(()->new RuntimeException("no patient"));
        System.out.println(patient);
        PatientRequest updatePatient = userMapper.toPatientRequest(appointmentRequest);
        userMapper.updatePatient(patient,updatePatient);
        System.out.println(patient);
        return patientRepository.save(patient);
    }
}
