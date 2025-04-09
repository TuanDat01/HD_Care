package com.doctorcare.PD_project.dto.response;

import com.doctorcare.PD_project.entity.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LikeResponse {

    String id;
    boolean status;
    BasicInfoUserResponse user;
}
