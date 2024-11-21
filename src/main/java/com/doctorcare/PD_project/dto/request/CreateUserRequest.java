package com.doctorcare.PD_project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    @NotNull(message = "FULL_NAME_NOT_BLANK")
    @Size(message = "FULL_NAME_SIZE")
    String name;
    @Email(message = "INVALID_FORMAT")
    String email;
    String username;
    @Size(min = 8,message = "PASSWORD_SIZE")
    String password;
    @Size(min = 10,max = 10, message = "PHONE_NUMBER_PATTERN")
    String phone;
    String address;
    String gender;
    boolean isEnable;
    String img;

}
