package com.doctorcare.PD_project.dto.request;

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
    String password; // Bạn có thể để lại tên này hoặc đổi thành password
    String phone;
    String gender;
    // Không cần thêm birthday, profileImg, role nếu không cần thiết
}
