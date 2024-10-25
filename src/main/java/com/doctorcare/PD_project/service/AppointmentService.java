package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.request.AppointmentV2Request;
import com.doctorcare.PD_project.dto.request.PatientRequest;
import com.doctorcare.PD_project.entity.*;
import com.doctorcare.PD_project.enums.AppointmentStatus;
import com.doctorcare.PD_project.mapping.AppointmentMapper;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.AppointmentRepository;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import com.doctorcare.PD_project.responsitory.PatientRepository;
import com.doctorcare.PD_project.responsitory.ScheduleRepository;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AppointmentService {
    DoctorRepository doctorRepository;
    PatientRepository patientRepository;
    ScheduleRepository scheduleRepository;
    AppointmentMapper appointmentMapper;
    AppointmentRepository appointmentRepository;
    UserMapper userMapper;
    PatientService patientService;
    SendEmailService emailService;
    public AppointmentV2Request getInfo(String idPatient, String idDoctor, String idSchedule){
        Patient patient1 = patientRepository.findById(idPatient).orElseThrow(()-> new RuntimeException("not found patient"));
        AppointmentV2Request appointmentV2Request = new AppointmentV2Request();
        PatientRequest patientRequest = userMapper.tPatientRequest(patient1);
        patientRequest.setId(idPatient);
        appointmentV2Request.setPatientRequest(patientRequest);
        appointmentV2Request.setDoctorScheduleRequest(scheduleRepository.getInfoSchedule(idSchedule,idDoctor));
        //        Appointment appointment = new Appointment();
//        appointment.setPatient(patient1);
//        appointment.setSchedule();
//        System.out.println(appointment);
        return appointmentV2Request;
    }

    @Transactional
    public AppointmentRequest createAppointment(AppointmentRequest appointmentRequest) throws MessagingException {
        // Lấy patient từ database
//        Patient patient = patientRepository.findById(appointmentRequest.getIdPatient())
//                .orElseThrow(() -> new RuntimeException("Patient not found"));
//        Patient updatedPatient = userMapper.toPatient(appointmentRequest);
//        System.out.println(patient.getEmail());
//        System.out.println(updatedPatient.getEmail());
//        updatePatientInfo(patient, updatedPatient);
        Patient patient = patientService.updatePatient(appointmentRequest);
        patientRepository.save(patient);
        Schedule schedule = scheduleRepository.findById(appointmentRequest.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        Appointment appointment = appointmentMapper.toAppointment(appointmentRequest);
        System.out.println(schedule.getId());
        Doctor doctor = doctorRepository.findDoctorBySchedules(schedule.getId());
        Prescription prescription = new Prescription();
        appointment.setPatient(patient);
        appointment.setSchedule(schedule);
        appointment.setDoctor(doctor);
        appointment.setStatus(AppointmentStatus.PENDING.toString());
        appointment.setPrescription(prescription);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        emailService.sendAppointmentConfirmation(appointment);
        return appointmentMapper.toAppointmentRequest(appointment);
    }


    public List<Appointment> findAllByPatientId(String id) {
        return appointmentRepository.findAllByPatientId(id);
    }
}
