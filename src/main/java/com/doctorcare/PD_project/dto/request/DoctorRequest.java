package com.doctorcare.PD_project.dto.request;

import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.Review;
import com.doctorcare.PD_project.entity.Schedule;
import com.doctorcare.PD_project.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorRequest {
    String location;

    String specialization;

    String experience;

    long price;

    String description;

}
