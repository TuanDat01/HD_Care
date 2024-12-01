package com.doctorcare.PD_project.service;


import com.doctorcare.PD_project.dto.request.CreateSchedule;
import com.doctorcare.PD_project.dto.request.DeleteScheduleRequest;
import com.doctorcare.PD_project.dto.request.DoctorScheduleRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.dto.response.ScheduleResponse;
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
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Validated
public class ScheduleService {
    DoctorRepository doctorRepository;
    UserMapper userMapper;
    ScheduleRepository scheduleRepository;
    AppointmentRepository appointmentRepository;
    ScheduleMapper scheduleMapper;
    @Transactional
    public DoctorResponse createSchedule(@Valid List<Schedule> scheduleRequest, String id) throws AppException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_DOCTOR));
        scheduleRequest.forEach((schedule -> {
            schedule.setAvailable(true);
            System.out.println(schedule);
            doctor.addSchedule(schedule);
            doctorRepository.save(doctor);
        }));
        DoctorResponse doctorResponse = userMapper.toDoctorResponse(doctor);
        List<ScheduleResponse> scheduleResponse = doctor.getSchedules().stream().map(schedule ->
                {
                    ScheduleResponse scheduleResponse1 = scheduleMapper.toScheduleResponse(schedule);

                    scheduleResponse1.setDate(schedule.getStart().toLocalDate());

                    return scheduleResponse1;
                }
                ).toList();

        doctorResponse.setSchedules(scheduleResponse);
        
        return doctorResponse;
    }

    public List<ScheduleResponse> getSchedule(String id, String date){
        List<Schedule> schedules =  scheduleRepository.findSchedule(id , date);

        return schedules.stream().map(schedule -> {
            ScheduleResponse scheduleResponse = scheduleMapper.toScheduleResponse(schedule);

            scheduleResponse.setDate(schedule.getStart().toLocalDate());

            return scheduleResponse;
        }).toList();
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
    public Schedule getScheduleById(String id) throws AppException {
        return scheduleRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND_SCHEDULE));
    }
    public DoctorScheduleRequest getInfoSchedule(String idSchedule,String idDoctor)
    {
        return scheduleRepository.getInfoSchedule(idSchedule,idDoctor);
    }
    @Transactional
    public ApiResponse<Void> deleteSchedule(String id, DeleteScheduleRequest scheduleRequest) throws AppException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_DOCTOR));
        List<Schedule> schedule = scheduleRequest.getScheduleList();
        for (Schedule schedule1 : schedule){
            Schedule schedule2 = scheduleRepository.findById(schedule1.getId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_SCHEDULE));
            if (doctor.getSchedules().contains(schedule2))
            {
                System.out.println("Innn");
                List<Appointment> appointmentRequests =  appointmentRepository.findAppointmentBySchedule(schedule1);
                for (Appointment appointmentRequest : appointmentRequests){
                    appointmentRequest.setStatus(AppointmentStatus.CANCELLED.toString());
                    appointmentRequest.setNote(scheduleRequest.getNote());
                }
            }
            doctor.getSchedules().remove(schedule2);

//            scheduleRepository.deleteById(schedule1.getId());
            System.out.println(doctor.getSchedules());
        }
        return null;
    }

    public List<Schedule> convertToSaveSchedule(CreateSchedule createSchedule) throws AppException {
        List<Schedule> schedules = scheduleRepository.findScheduleByDate(createSchedule.getDate());
        Doctor doctor = doctorRepository.findById(createSchedule.getIdDoctor())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_DOCTOR));
        System.out.println(doctor.getUsername());
// Danh sách lịch từ Doctor
        List<Schedule> filter = doctor.getSchedules();
        System.out.println(filter.toString());
// Giữ lại các phần tử trong filter nếu chúng tồn tại trong schedules
        List<Schedule> filteredSchedules = schedules.stream()
                .filter(schedule -> filter.stream().anyMatch(schedule1 -> schedule1.equals(schedule)))
                .toList();

        List<ScheduleResponse> scheduleResponses = filteredSchedules.stream().map(scheduleMapper::toScheduleResponse).toList();
        return createSchedule.getSchedules().stream().map(s -> {
                String[] parts = s.split("-");

                if (parts.length == 2) {
                    for (ScheduleResponse scheduleResponse: scheduleResponses) {
                        if (Objects.equals(parts[0], scheduleResponse.getStart())){
                            throw new RuntimeException("START_TIME_EXISTED");
                        }
                    }
                    LocalDate localDate = LocalDate.parse(createSchedule.getDate()); // Parse từ chuỗi
                    LocalTime startLocalTime = LocalTime.parse(parts[0]); // Parse từ chuỗi
                    LocalTime endLocalTime = LocalTime.parse(parts[1]); // Parse từ chuỗi
                    LocalDateTime startDateTime = LocalDateTime.of(localDate, startLocalTime);
                    LocalDateTime endDateTime = LocalDateTime.of(localDate, endLocalTime);
                    if ((Duration.between(startDateTime, endDateTime).toHours() != 1)) {
                        throw new RuntimeException("START_TIME_EXISTED");
                    }
                    System.out.println(Schedule.builder().end(endDateTime).start(startDateTime).build().toString());
                    return Schedule.builder().end(endDateTime).start(startDateTime).build();
                } else {
                    System.out.println("Input format is incorrect");
                }
                    return null;
                }
            ).toList();
    }

    public DoctorResponse saveSchedule(CreateSchedule createSchedule,String idDoctor) throws AppException {
        return createSchedule(convertToSaveSchedule(createSchedule),idDoctor);
    }
}
