package com.doctorcare.PD_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentV2Request {

    @NotBlank(message = "Mô tả không được để trống.")
    String description;

    @NotBlank(message = "Tiêu đề không được để trống.")
    String title;

    PatientRequest patientRequest;
    DoctorScheduleRequest doctorScheduleRequest;
}
