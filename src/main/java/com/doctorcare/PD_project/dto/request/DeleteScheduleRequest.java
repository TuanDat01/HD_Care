package com.doctorcare.PD_project.dto.request;

import com.doctorcare.PD_project.entity.Schedule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteScheduleRequest {
    List<Schedule> scheduleList;
    String note;
}
