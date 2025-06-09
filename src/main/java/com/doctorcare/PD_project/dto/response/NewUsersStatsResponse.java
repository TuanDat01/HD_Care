package com.doctorcare.PD_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewUsersStatsResponse {
    private Long newPatients;
    private Long newDoctors;
}
