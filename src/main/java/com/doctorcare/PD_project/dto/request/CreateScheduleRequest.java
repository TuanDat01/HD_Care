package com.doctorcare.PD_project.dto.request;
import java.time.LocalDateTime;
import java.util.Date;

import com.doctorcare.PD_project.validation.ScheduleConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;



@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CreateScheduleRequest {

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
//    @ScheduleConstraint(message = "DATE_INVALID")
//    private LocalDateTime start;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
//    private LocalDateTime end;
    String start;
    String end;
}
