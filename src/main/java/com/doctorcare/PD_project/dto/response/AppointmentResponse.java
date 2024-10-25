package com.doctorcare.PD_project.dto.response;

import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Prescription;
import com.doctorcare.PD_project.entity.Schedule;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentResponse {
    String description;
    Schedule schedule;
    Prescription prescription;
    Doctor doctor;

}
