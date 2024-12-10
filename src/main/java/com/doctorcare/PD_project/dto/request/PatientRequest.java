package com.doctorcare.PD_project.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PatientRequest {
    String id;
    String name;
    String dob;
    String gender;
    @Size(min = 10, max = 10, message = "PHONE_NUMBER_PATTERN")
    String phone;
    String address;
    String img;
    String username;
    @Email(message = "INVALID_FORMAT")
    String email;
}
