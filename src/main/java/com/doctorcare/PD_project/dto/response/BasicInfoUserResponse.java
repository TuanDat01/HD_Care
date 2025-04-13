package com.doctorcare.PD_project.dto.response;

import lombok.Data;

@Data
public class BasicInfoUserResponse {
    private String id;
    private String name;
    private String username;
    private String avatar;
    private String role;

    /** true nếu current user đang follow user này */
    private boolean followed;
}
