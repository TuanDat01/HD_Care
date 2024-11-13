package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.entity.Schedule;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor-schedule")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Validated
public class ScheduleController {
    ScheduleService scheduleService;
    @PostMapping
    public ApiResponse<DoctorResponse> createSchedule(@RequestParam(name = "idDoctor",required = true) String id, @Valid @RequestBody List<Schedule> schedule) throws AppException {
        DoctorResponse doctorResponse = scheduleService.createSchedule(schedule, id);
        return ApiResponse.<DoctorResponse>builder().result(doctorResponse).build();
    }
    @GetMapping
    public ApiResponse<List<Schedule>> getSchedule(@RequestParam(name = "idDoctor",required = true) String id,@RequestParam(name = "date",required = false) String date){
        return ApiResponse.<List<Schedule>>builder().result(scheduleService.getSchedule(id,date)).build();
    }
    @PostMapping("/delete-schedules")
    public ApiResponse<Void> deleteSchedule(@RequestParam(name = "idDoctor",required = true) String id, @Valid @RequestBody List<Schedule> schedule) throws AppException {
        return scheduleService.deleteSchedule(id,schedule);
    }
    @GetMapping("/{id}")
    public ApiResponse<Schedule> getScheduleById(@PathVariable String id) throws AppException {
        return ApiResponse.<Schedule>builder().result(scheduleService.getScheduleById(id)).build();
    }

    @PostMapping("/schedule-inactive")
    public ApiResponse<DoctorResponse> getScheduleInactive(@RequestParam(name = "idDoctor",required = true) String id, @RequestBody List<Schedule> schedule) throws AppException {
        return ApiResponse.<DoctorResponse>builder().result(scheduleService.getScheduleInactive(schedule,id)).build();
    }


}
