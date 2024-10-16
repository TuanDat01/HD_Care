package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.UpdateDoctorRequest;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class DoctorService {
    DoctorRepository doctorRepository;
    UserMapper userMapper;

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

    public DoctorResponse UpdateInfo(String id, UpdateDoctorRequest doctorRequest) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()-> new RuntimeException("Not found doctor"));
        userMapper.updateDoctor(doctorRequest,doctor);
        return userMapper.toDoctorResponse(doctorRepository.save(doctor));
    }

    public List<DoctorResponse> FilterByProvince(String province) {
        List<Doctor> doctors = doctorRepository.findByLocation(province);
        return TransformDoctorResponse(doctors);
    }

    public List<DoctorResponse> FilterByName(String name) {
        List<Doctor> doctors = doctorRepository.findByNameContaining(name);
        return TransformDoctorResponse(doctors);
    }

    public List<DoctorResponse> OrderPriceAsc() {
        List<Doctor> doctors = doctorRepository.findAllByOrderByPriceAsc();
        return TransformDoctorResponse(doctors);
    }

    public List<DoctorResponse> OrderPriceDesc() {
        List<Doctor> doctors = doctorRepository.findAllByOrderByPriceDesc();
        return TransformDoctorResponse(doctors);
    }

    public List<DoctorResponse> GetAll(){
        List<Doctor> doctors = doctorRepository.findAll();
        return TransformDoctorResponse(doctors);
    }

    public DoctorResponse FindDoctorById(String id)
    {
        return userMapper.toDoctorResponse(doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found doctor")));
    }


}
