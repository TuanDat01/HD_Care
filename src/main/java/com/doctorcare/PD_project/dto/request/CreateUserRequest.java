package com.doctorcare.PD_project.dto.request;

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
    String phone;
    String gender;
}
