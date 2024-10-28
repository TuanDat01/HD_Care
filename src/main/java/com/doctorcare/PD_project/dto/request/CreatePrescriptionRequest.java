package com.doctorcare.PD_project.dto.request;

import com.doctorcare.PD_project.entity.MedicineDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePrescriptionRequest {
    String result;
    LocalDate timestamp;
    List<MedicineDetail> medicineDetails;

}
