package com.doctorcare.PD_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppointmentStatsResponse {
    private String doctorId;
    private String doctorName;
    private String status;
    private Long count;
}
