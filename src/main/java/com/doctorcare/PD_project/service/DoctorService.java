package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.DoctorRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class DoctorService {
    DoctorRepository doctorRepository;
    UserMapper userMapper;

    public DoctorResponse UpdateInfo(String id, DoctorRequest doctorRequest) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()-> new RuntimeException("Not found doctor"));
        userMapper.updateDoctor(doctorRequest,doctor);
        return userMapper.toDoctorResponse(doctorRepository.save(doctor));
    }

    public List<DoctorResponse> FilterByProvince(String province) {
        List<Doctor> doctors = doctorRepository.findByLocation(province);
        return doctors.stream().map(userMapper::toDoctorResponse).toList();
    }

    public List<DoctorResponse> FilterByName(String name) {
        List<Doctor> doctors = doctorRepository.findByNameContaining(name);
        return doctors.stream().map(userMapper::toDoctorResponse).toList();
    }

    public List<DoctorResponse> OrderPriceAsc() {
        List<Doctor> doctors = doctorRepository.findAllByOrderByPriceAsc();
        return doctors.stream().map(userMapper::toDoctorResponse).toList();
    }

    public List<DoctorResponse> OrderPriceDesc() {
        List<Doctor> doctors = doctorRepository.findAllByOrderByPriceDesc();
        return doctors.stream().map(userMapper::toDoctorResponse).toList();
    }


}
