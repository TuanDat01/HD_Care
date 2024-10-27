package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.CreateScheduleRequest;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.dto.response.FindScheduleResponse;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Schedule;
import com.doctorcare.PD_project.mapping.ScheduleMapper;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import com.doctorcare.PD_project.responsitory.ScheduleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ScheduleService {
    DoctorRepository doctorRepository;
    ScheduleMapper scheduleMapper;
    UserMapper userMapper;
    ScheduleRepository scheduleRepository;
    @Transactional
    public DoctorResponse createSchedule(List<Schedule> scheduleRequest, String id){
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()->new RuntimeException("not found"));
//        List<Schedule> schedules = scheduleRequest.stream().map((scheduled)->{
//            Schedule schedule = scheduleMapper.toSchedule(scheduled);
//            schedule.setAvailable(true);
//            return schedule;
//        }).toList();
//        System.out.println(schedules+"++++++++++");
        scheduleRequest.forEach((schedule -> {
            schedule.setAvailable(true);
            doctor.addSchedule(schedule);
        }));
        return userMapper.toDoctorResponse(doctor);
    }

    public List<Schedule> getSchedule(String id){
        return scheduleRepository.findSchedule(id);
    }

    @Transactional
    public DoctorResponse getScheduleUnactive(List<Schedule> scheduleRequest, String id){
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()->new RuntimeException("not found"));
        System.out.println("inn");
        scheduleRequest.forEach((schedule -> {
                schedule.setAvailable(false);
                System.out.println(schedule);
                doctor.addSchedule(schedule);
        }));
        return userMapper.toDoctorResponse(doctor);
    }

    public CreateScheduleRequest getScheduleById(String id){
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(()-> new RuntimeException("no find schedule"));
        return scheduleMapper.toScheduleRequest(schedule);
    }

}
