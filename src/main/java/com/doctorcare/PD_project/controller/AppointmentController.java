package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.request.AppointmentV2Request;
import com.doctorcare.PD_project.dto.request.DoctorScheduleRequest;
import com.doctorcare.PD_project.dto.request.UpdateStatusAppointment;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.ManagePatient;
import com.doctorcare.PD_project.dto.response.PageResponse;
import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.enums.AppointmentStatus;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.event.create.CompleteAppointment;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.AppointmentService;
import com.doctorcare.PD_project.service.SendEmailService;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/appointment")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AppointmentController {
    AppointmentService appointmentService;
    SendEmailService sendEmailService;
    ApplicationEventPublisher applicationEventPublisher;

    @GetMapping
    public ApiResponse<AppointmentV2Request> getForm(@RequestParam(name = "idPatient")String idPatient,
                                                     @RequestParam(name = "idSchedule")String idSchedule,
                                                     @RequestParam(name = "idDoctor")String idDoctor) throws AppException {
        return ApiResponse.<AppointmentV2Request>builder().result(appointmentService.getInfo(idPatient,idSchedule,idDoctor)).build();
    }

    @PostMapping
    public ApiResponse<AppointmentRequest> create(@RequestBody @Valid AppointmentRequest appointmentRequest) throws MessagingException, AppException {
        return ApiResponse.<AppointmentRequest>builder().result(appointmentService.createAppointment(appointmentRequest)).build();
    }

    @GetMapping("/patient-appointment")
    public ApiResponse<Page<AppointmentRequest>> getAppointmentByPatient(@RequestParam(name = "patientId") String id,
                                                                         @RequestParam(name = "date",required = false) String date,
                                                                         @RequestParam(name = "week",required = false) String week,
                                                                         @RequestParam(name = "month",required = false) String month,
                                                                         @RequestParam(name = "status", required = false) String status,
                                                                         @RequestParam(name = "page",required = false) int page)
    {
        System.out.println(id);
        return ApiResponse.<Page<AppointmentRequest>>builder().result(appointmentService.findAllByPatientId(id,date, week, month,status,page-1)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AppointmentRequest> getAppointmentById(@PathVariable String id) throws AppException {
        return ApiResponse.<AppointmentRequest>builder().result(appointmentService.getAppointmentById(id)).build();
    }

    @GetMapping("/doctor-appointments")
    public ApiResponse<Page<AppointmentRequest>> getAppointmentByDoctor(@RequestParam(name = "doctorId") String id,
                                                                        @RequestParam(name = "date",required = false) String date,
                                                                        @RequestParam(name = "status",required = false) String status,
                                                                        @RequestParam(name = "page", required = false) int page,
                                                                        @RequestParam(name = "name",required = false) String name) throws AppException {
        return ApiResponse.<Page<AppointmentRequest>>builder().result(appointmentService.getAppointmentByDoctor(id, date, status,page - 1,name)).build();
    }

    @PutMapping("/doctor-appointment/{id}/status")
    public ApiResponse<Appointment> getAppointmentByDoctorAndId(@PathVariable String id, @RequestBody UpdateStatusAppointment updateStatusAppointment) throws AppException {
        return ApiResponse.<Appointment>builder().result(appointmentService.getAppointmentByDoctorAndId(id, updateStatusAppointment)).build();
    }

    @GetMapping("/doctor-appointments/time")
    public ApiResponse<Page<AppointmentRequest>> filterAppointment(@RequestParam (name = "doctorId") String id,
                                                                   @RequestParam (name = "week", required = false) String week,
                                                                   @RequestParam(name = "month", required = false) String month,
                                                                   @RequestParam(name = "status", required = false) String status,
                                                                   @RequestParam (name = "page", required = true) Integer page,
                                                                   @RequestParam(name = "name", required = false) String name) throws AppException {
        return ApiResponse.<Page<AppointmentRequest>>builder()
                .result(appointmentService.filterAppointment(id, week, month, status, page - 1, name))
                .build();
    }

    @GetMapping("/doctor-appointment/manage-patient")
    public ApiResponse<Page<ManagePatient>> getPatientOfDoctor(@RequestParam(name = "doctorId") String id,
                                                               @RequestParam(name = "page") Integer page,
                                                               @RequestParam(name = "keyword", required = false) String keyword) throws AppException {
        return ApiResponse.<Page<ManagePatient>>builder().result(appointmentService.getPatientOfDoctor(id, page - 1, keyword)).build();
    }

    @GetMapping("/pdf/{appointmentId}")
    public ResponseEntity<byte[]> exportToPdf(@PathVariable("appointmentId") String appointmentId,
                                              @RequestParam("status") String status) throws AppException {

        AppointmentRequest appointmentRequest = appointmentService.getData(appointmentId);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            appointmentService.createPdf(appointmentId, outputStream);

            byte[] pdfData = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", appointmentId + ".pdf");

            if (Objects.equals(appointmentRequest.getStatus(), AppointmentStatus.PENDING.toString())){
                throw new AppException(ErrorCode.UPDATE_STATUS);
            }

            if (Objects.equals(status, "Gửi"))
                applicationEventPublisher.publishEvent(new CompleteAppointment(appointmentRequest, pdfData));

            return ResponseEntity.ok().headers(headers).body(pdfData);
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
