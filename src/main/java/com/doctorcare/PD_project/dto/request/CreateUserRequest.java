package com.doctorcare.PD_project.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    String name;
    String email;
    String username;
    @Size(min = 8,message = "PASSWORD_SIZE")
    String password;
    @Size(min = 10, message = "PHONE_NUMBER_PATTERN")
    String phone;
    String gender;
    boolean isEnable;
    String img;

}
