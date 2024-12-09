package com.doctorcare.PD_project.dto.request;

import com.doctorcare.PD_project.entity.Medicine;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePrescriptionRequest {
    String result;
    LocalDateTime timestamp;
    List<Medicine> medicines;

}
