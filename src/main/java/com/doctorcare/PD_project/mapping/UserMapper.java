package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.request.PatientRequest;
import com.doctorcare.PD_project.dto.request.UpdateDoctorRequest;
import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.dto.response.UserGoogleResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "pwd",target = "password")
    UserResponse toUserResponse(User user);

    @Mapping(source = "password",target = "pwd")
    Doctor toDoctor(CreateUserRequest userRequest);

    @Mapping(target = "pwd",ignore = true)
    Patient toPatient(CreateUserRequest userRequest);
    @Mapping(source = "verifiedEmail", target = "enable")
    @Mapping(source = "picture", target = "img")
    @Mapping(target = "pwd",ignore = true)
    Patient toPatient(UserGoogleResponse userGoogleResponse);

    void updateDoctor(UpdateDoctorRequest doctorRequest, @MappingTarget Doctor doctor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "schedules",ignore = true)
    DoctorResponse toDoctorResponse(Doctor doctor);

    @InheritInverseConfiguration
    Doctor toDoctor(DoctorResponse doctorResponse);

    PatientRequest tPatientRequest(Patient patient);

    @Mapping(target = "id",ignore = true)
    void updatePatient(@MappingTarget Patient patient, PatientRequest updatePatient);

    PatientRequest toPatientRequest(AppointmentRequest appointmentRequest);
    PatientRequest toPatientRequest(Patient patient);
    @Mapping(source = "pwd",target = "password")
    UserResponse toUserResponse(Patient patient);


}
