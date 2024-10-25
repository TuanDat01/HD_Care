package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.CreateScheduleRequest;
import com.doctorcare.PD_project.entity.Schedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule toSchedule(CreateScheduleRequest scheduleRequest);
    CreateScheduleRequest toScheduleRequest(Schedule schedule);
}
