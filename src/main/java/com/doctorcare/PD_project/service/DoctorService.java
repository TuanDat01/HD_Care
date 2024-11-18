package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.request.DoctorPageRequest;
import com.doctorcare.PD_project.dto.request.UpdateDoctorRequest;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.dto.response.PageResponse;
import com.doctorcare.PD_project.dto.response.ScheduleResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Schedule;
import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.enums.Roles;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.ScheduleMapper;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import com.doctorcare.PD_project.responsitory.ScheduleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class DoctorService {
    DoctorRepository doctorRepository;
    UserMapper userMapper;
    ScheduleMapper scheduleMapper;
    ScheduleRepository scheduleRepository;
    PasswordEncoder passwordEncoder;
    @Transactional
    public UserResponse CreateDoctor(CreateUserRequest userRequest) throws AppException {
        if(doctorRepository.findDoctorByUsername(userRequest.getUsername()).isEmpty()){
            throw new AppException(ErrorCode.NOT_FOUND_DOCTOR);
        }
        Doctor doctor = userMapper.toDoctor(userRequest);
        doctor.setRole(Roles.DOCTOR.name());
        doctor.setPwd(passwordEncoder.encode(userRequest.getPassword()));
        Doctor savedDoctor = doctorRepository.save(doctor);
        return userMapper.toUserResponse(savedDoctor);
    }
    public List<ScheduleResponse> convertScheduleOfDoctor(Doctor doctor){
        List<ScheduleResponse> scheduleResponses = new ArrayList<>();

        for (Schedule s:doctor.getSchedules()
        ) {
            ScheduleResponse scheduleResponse = scheduleMapper.toScheduleResponse(s);
            scheduleResponse.setDate(LocalDate.now());

            scheduleResponses.add(scheduleResponse);
        }
        return scheduleResponses;
    }
    public List<DoctorResponse> TransformDoctorResponse(List<Doctor> doctors){
        List<DoctorResponse> doctorResponses = doctors.stream().map(doctor -> {
            List<ScheduleResponse> scheduleResponses = convertScheduleOfDoctor(doctor);

            DoctorResponse doctorResponse = userMapper.toDoctorResponse(doctor);
            doctorResponse.setSchedules(scheduleResponses);

            return  doctorResponse;
        }).toList();

        for (int i=0;i<doctorResponses.size();i++){
            Doctor doctor = doctors.get(i);
            DoctorResponse doctorResponse = doctorResponses.get(i);

            doctorResponse.setId(doctor.getId());
        }
        return doctorResponses;
    }
    @Transactional
    public DoctorResponse UpdateInfo(String id, UpdateDoctorRequest doctorRequest) throws AppException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND_DOCTOR));
        userMapper.updateDoctor(doctorRequest,doctor);
        return userMapper.toDoctorResponse(doctorRepository.save(doctor));
    }

    public PageResponse GetAll(String district, String name, String city, int p) throws AppException {
        List<Doctor> doctors = null;
        int limit = 3;
        long pageMax = doctorRepository.count()/limit;

        if( p-1 < 0 || p-1 >= pageMax){
            throw new AppException(ErrorCode.PAGE_VALID);
        }

        DoctorPageRequest doctorPageRequest = new DoctorPageRequest(limit, (p-1)*limit);

        if (district == null && name == null && city == null) {
            doctorPageRequest = new DoctorPageRequest(limit, (p-1) * limit);

            Page<Doctor> pageDoctor = doctorRepository.findAll(doctorPageRequest);
            doctors = pageDoctor.getContent();

            List<DoctorResponse> doctorResponse = TransformDoctorResponse(doctors);

            return PageResponse.builder().pageMax(pageMax).doctorResponse(doctorResponse).build();
        }
        doctors = doctorRepository.filterDoctor(district,name,city,doctorPageRequest).getContent();

        doctors = doctors.stream().peek(doctor -> {
            List<Schedule> schedule = scheduleRepository.findSchedule(doctor.getId(), LocalDate.now().toString());
            doctor.setSchedules(schedule);
        }).toList();

        List<DoctorResponse> doctorResponse = TransformDoctorResponse(doctors);

        return PageResponse.builder().pageMax(pageMax).doctorResponse(doctorResponse).build();
    }

    public DoctorResponse FindDoctorResponseById(String id) throws AppException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_DOCTOR));

        List<ScheduleResponse> scheduleResponses = convertScheduleOfDoctor(doctor);

        DoctorResponse doctorResponse = userMapper.toDoctorResponse(doctor);
        doctorResponse.setSchedules(scheduleResponses);

        return  doctorResponse;


    }

    public Doctor findDoctorById(String id) throws AppException {
        return doctorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_DOCTOR));
    }
    public Doctor findDoctorBySchedules(String id){
        return doctorRepository.findDoctorBySchedules(id);
    }

    public void update(Doctor doctor) {

    }
}
