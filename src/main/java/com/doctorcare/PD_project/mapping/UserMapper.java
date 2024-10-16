package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.UpdateDoctorRequest;
import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.response.DoctorResponse;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "pwd",target = "password")
    UserResponse toUserResponse(User user);
    @Mapping(source = "password",target = "pwd")

    Doctor toDoctor(CreateUserRequest userRequest);


    void updateDoctor(UpdateDoctorRequest doctorRequest, @MappingTarget Doctor doctor);
    @Mapping(target = "id", ignore = true)

    DoctorResponse toDoctorResponse(Doctor doctor);
}
