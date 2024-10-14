package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.DoctorRequest;
import com.doctorcare.PD_project.dto.request.UserRequest;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "pwd",target = "password")
    UserResponse toUserResponse(User user);
    @Mapping(source = "password",target = "pwd")

    Doctor toDoctor(UserRequest userRequest);


    void updateDoctor(DoctorRequest doctorRequest, @MappingTarget Doctor doctor);

    DoctorResponse toDoctorResponse(Doctor doctor);
}
