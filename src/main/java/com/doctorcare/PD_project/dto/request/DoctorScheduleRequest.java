package com.doctorcare.PD_project.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorScheduleRequest {
    String id;
    String name;
    String profile_img;
    String email;
    LocalDateTime startTime;
    LocalDateTime endTime;


}
