package com.doctorcare.PD_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorSummaryResponse {
    private String id;
    private String name;
    private String username;
}
