package com.doctorcare.PD_project.dto.response;

import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.entity.Post;
import com.doctorcare.PD_project.entity.User;
import lombok.Data;

@Data
public class NotificateDTO {
    User user;
    BasicInfoUserResponse userResponse;
    Patient patient;
    Post post;
}
