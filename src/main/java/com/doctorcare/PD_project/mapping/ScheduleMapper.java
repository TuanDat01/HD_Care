package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.CreateScheduleRequest;
import com.doctorcare.PD_project.dto.response.ScheduleResponse;
import com.doctorcare.PD_project.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule toSchedule(CreateScheduleRequest scheduleRequest);
    CreateScheduleRequest toScheduleRequest(Schedule schedule);

    @Mapping(source = "start",target = "start",dateFormat = "dd-MM-yyyy hh:mm")
    @Mapping(source = "end",target = "end",dateFormat = "dd-MM-yyyy hh:mm")
    ScheduleResponse toScheduleResponse(Schedule schedule);
}
