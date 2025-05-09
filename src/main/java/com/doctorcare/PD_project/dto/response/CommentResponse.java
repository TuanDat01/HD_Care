package com.doctorcare.PD_project.dto.response;

import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.entity.Post;
import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.mapping.HasUser;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse implements HasUser {
    private String id;
    private String content;
    private LocalDateTime createdAt;
    private BasicInfoUserResponse userResponse;
    private Post post;

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public Patient getPatient() {
        return null;
    }
}
