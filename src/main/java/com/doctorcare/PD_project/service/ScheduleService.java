package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.request.DoctorScheduleRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Schedule;
import com.doctorcare.PD_project.enums.AppointmentStatus;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.ScheduleMapper;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.AppointmentRepository;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import com.doctorcare.PD_project.responsitory.ScheduleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ScheduleService {
    DoctorRepository doctorRepository;
    UserMapper userMapper;
    ScheduleRepository scheduleRepository;
    AppointmentRepository appointmentRepository;
    @Transactional
    public DoctorResponse createSchedule(List<Schedule> scheduleRequest, String id) throws AppException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_DOCTOR));
        scheduleRequest.forEach((schedule -> {
            schedule.setAvailable(true);
            doctor.addSchedule(schedule);
        }));
        return userMapper.toDoctorResponse(doctor);
    }

    public List<Schedule> getSchedule(String id,String date){
        return scheduleRepository.findSchedule(id , date);
    }

    @Transactional
    public DoctorResponse getScheduleInactive(List<Schedule> scheduleRequest, String id) throws AppException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_DOCTOR));
        scheduleRequest.forEach((schedule -> {
            Schedule schedule1 = null;
            try {
                schedule1 = scheduleRepository.findById(schedule.getId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_SCHEDULE));
            } catch (AppException e) {
                throw new RuntimeException(e);
            }
            schedule1.setAvailable(false);
        }));
        return userMapper.toDoctorResponse(doctor);
    }

    public Schedule getScheduleById(String id){
        return scheduleRepository.findById(id).orElseThrow(()-> new RuntimeException("no find schedule"));
    }
    public DoctorScheduleRequest getInfoSchedule(String idSchedule,String idDoctor)
    {
        return scheduleRepository.getInfoSchedule(idSchedule,idDoctor);
    }
    @Transactional
    public ApiResponse<Void> deleteSchedule(String id, List<Schedule> schedule) throws AppException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_DOCTOR));
        for (Schedule schedule1 : schedule){
            if (doctor.getSchedules().contains(schedule1))
            {
                List<Appointment> appointmentRequests =  appointmentRepository.findAppointmentBySchedule(schedule1);
                for (Appointment appointmentRequest : appointmentRequests){
                    appointmentRequest.setStatus(AppointmentStatus.CANCELLED.toString());
                }
            }
            doctor.getSchedules().remove(schedule1);
        }
        return null;
    }
}
