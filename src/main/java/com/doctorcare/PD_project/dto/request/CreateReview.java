package com.doctorcare.PD_project.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateReview {
    String content;
    double rating;
    List<String> img;
    String idAppointment;
}
