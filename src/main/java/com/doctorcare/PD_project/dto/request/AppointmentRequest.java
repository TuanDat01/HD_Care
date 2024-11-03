package com.doctorcare.PD_project.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentRequest {
    String id;
    String idPatient;
    String name;
    String gender;
    String address;
    String description;
    String title;
    String scheduleId;
    String email;
    String status;
    String start;
    String end;
    LocalDate dob;
    String prescriptionId;
    String nameDoctor;
}
