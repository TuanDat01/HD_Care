package com.doctorcare.PD_project.dto.request;

import com.doctorcare.PD_project.validation.ScheduleConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSchedule {

    String date;
    List<String> schedules;
    String idDoctor;
}
