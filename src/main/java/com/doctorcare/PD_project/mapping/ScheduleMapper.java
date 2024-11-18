package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.CreateScheduleRequest;
import com.doctorcare.PD_project.dto.response.ScheduleResponse;
import com.doctorcare.PD_project.entity.Schedule;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule toSchedule(CreateScheduleRequest scheduleRequest);
    CreateScheduleRequest toScheduleRequest(Schedule schedule);

    @Mapping(source = "start",target = "start",dateFormat = "HH:mm")
    @Mapping(source = "end",target = "end",dateFormat = "HH:mm")
    ScheduleResponse toScheduleResponse(Schedule schedule);


}
