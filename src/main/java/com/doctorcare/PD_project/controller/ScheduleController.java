package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.CreateScheduleRequest;
import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.dto.response.FindScheduleResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Schedule;
import com.doctorcare.PD_project.service.DoctorService;
import com.doctorcare.PD_project.service.PatientService;
import com.doctorcare.PD_project.service.ScheduleService;
import com.doctorcare.PD_project.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor-schedule")
@CrossOrigin(origins = "http://localhost:3000") // Chỉ cho phép localhost:3000 truy cập
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class ScheduleController {
    ScheduleService scheduleService;
    @PostMapping
    public ApiResponse<DoctorResponse> createSchedule(@RequestParam(name = "idDoctor",required = true) String id, @RequestBody List<Schedule> schedule){
        DoctorResponse doctorResponse = scheduleService.createSchedule(schedule, id);
        return ApiResponse.<DoctorResponse>builder().result(doctorResponse).build();
    }
    @GetMapping
    public ApiResponse<List<Schedule>> getSchedule(@RequestParam(name = "idDoctor",required = true) String id){
        return ApiResponse.<List<Schedule>>builder().result(scheduleService.getSchedule(id)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CreateScheduleRequest> getScheduleById(@PathVariable String id){
        return ApiResponse.<CreateScheduleRequest>builder().result(scheduleService.getScheduleById(id)).build();
    }

    @PostMapping("/schedule-inactive")
    public ApiResponse<DoctorResponse> getScheduleUnactive(@RequestParam(name = "idDoctor",required = true) String id, @RequestBody List<Schedule> schedule){
        return ApiResponse.<DoctorResponse>builder().result(scheduleService.getScheduleUnactive(schedule,id)).build();
    }
}
