package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.response.BasicInfoUserResponse;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.entity.Post;
import com.doctorcare.PD_project.entity.User;

public interface HasUser {
    User getUser();
    BasicInfoUserResponse getUserResponse();
    Patient getPatient();
    Post getPost();
}
