package com.doctorcare.PD_project.dto.response;

import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.entity.Review;
import com.doctorcare.PD_project.entity.Schedule;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorResponse {
    String id;
    String name;
    String clinicName;
    String district;
    String city;
    String email;
    String specialization;
    String experience;
    long price;
    String description;
    List<ScheduleResponse> schedules;
    List<Review> reviews;
    String img;
    String address;
    boolean enable;


}
