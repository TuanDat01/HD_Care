package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.CreateScheduleRequest;
import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.service.DoctorService;
import com.doctorcare.PD_project.service.PatientService;
import com.doctorcare.PD_project.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor-schedule")
@CrossOrigin(origins = "http://localhost:3000") // Chỉ cho phép localhost:3000 truy cập
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class ScheduleController {
    DoctorService doctorService;
    @PostMapping
    public ApiResponse<DoctorResponse> getDoctor(@RequestParam(name = "id",required = true) String id, @RequestBody CreateScheduleRequest schedule){
        DoctorResponse doctorResponse = doctorService.createSchedule(schedule, id);
        return ApiResponse.<DoctorResponse>builder().result(doctorResponse).build();
    }
}
