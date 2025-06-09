package com.doctorcare.PD_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VisitStatsResponse {
    private String doctorId;
    private String doctorName;
    private LocalDate date;
    private Long visits;

    public VisitStatsResponse(String doctorId, String doctorName, Date date, Long visits) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.date = date.toLocalDate();
        this.visits = visits;
    }
}
