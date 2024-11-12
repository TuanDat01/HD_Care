package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.request.DoctorPageRequest;
import com.doctorcare.PD_project.dto.request.UpdateDoctorRequest;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.enums.Roles;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class DoctorService {
    DoctorRepository doctorRepository;
    UserMapper userMapper;

    public UserResponse CreateDoctor(CreateUserRequest userRequest) {

        Doctor doctor = userMapper.toDoctor(userRequest);
        doctor.setRole(Roles.DOCTOR.name());
        Doctor savedDoctor = doctorRepository.save(doctor);
        return userMapper.toUserResponse(savedDoctor);
    }

    public List<DoctorResponse> TransformDoctorResponse(List<Doctor> doctors){
        Stream<Doctor> doctorStream = doctors.stream();
        List<DoctorResponse> doctorResponses = doctorStream.map(userMapper::toDoctorResponse).toList();
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

    public List<DoctorResponse> GetAll(String district, String name, String city, String p) {
        List<Doctor> doctors = null;
        System.out.println(LocalDateTime.now());

        int limit = 3;
        DoctorPageRequest doctorPageRequest = new DoctorPageRequest(limit, Integer.parseInt(p));
//        if (p==null){
//            long countDoctor = doctorRepository.count();
//            System.out.println(countDoctor);
//            doctorPageRequest = new DoctorPageRequest((int) countDoctor,0);
//        }
        if (district == null && name == null && city == null) {
            doctorPageRequest = new DoctorPageRequest(limit, Integer.parseInt(p));
            Page<Doctor> pageDoctor = doctorRepository.findAll(doctorPageRequest);
            doctors = pageDoctor.getContent();
            return TransformDoctorResponse(doctors);
        }
        doctors = doctorRepository.filterDoctor(district,name,city,doctorPageRequest).getContent();
        return TransformDoctorResponse(doctors);
    }

    public DoctorResponse FindDoctorResponseById(String id) throws AppException {
        return userMapper.toDoctorResponse(doctorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_DOCTOR)));
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
