package com.doctorcare.PD_project.dto.response;

import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Prescription;
import com.doctorcare.PD_project.entity.Schedule;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    String id;
    String content;
    boolean checkSelf;
    Instant timestamp;

}
