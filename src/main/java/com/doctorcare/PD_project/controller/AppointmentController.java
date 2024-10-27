package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.request.AppointmentV2Request;
import com.doctorcare.PD_project.dto.request.DoctorScheduleRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.AppointmentService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
@CrossOrigin(origins = "http://localhost:3000") // Chỉ cho phép localhost:3000 truy cập
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AppointmentController {
    AppointmentService appointmentService;
    @GetMapping
    public ApiResponse<AppointmentV2Request> getForm(@RequestParam(name = "idPatient")String idPatient, @RequestParam(name = "idSchedule")String idSchedule, @RequestParam(name = "idDoctor")String idDoctor){
        return ApiResponse.<AppointmentV2Request>builder().result(appointmentService.getInfo(idPatient,idSchedule,idDoctor)).build();
    }
    @PostMapping
    public ApiResponse<AppointmentRequest> create(@RequestBody AppointmentRequest appointmentRequest) throws MessagingException, AppException {
        return ApiResponse.<AppointmentRequest>builder().result(appointmentService.createAppointment(appointmentRequest)).build();
    }

    @GetMapping("/patient-appointment")
    public ApiResponse<List<Appointment>> getAppointmentByPatient(@RequestParam(name = "patientId") String id){
        System.out.println(id);
        return ApiResponse.<List<Appointment>>builder().result(appointmentService.findAllByPatientId(id)).build();
    }
}
