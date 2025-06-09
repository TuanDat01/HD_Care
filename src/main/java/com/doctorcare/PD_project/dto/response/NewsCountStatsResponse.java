package com.doctorcare.PD_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class NewsCountStatsResponse {
    private String doctorId;
    private String doctorName;
    private LocalDate date;
    private Long newsCount;

    public NewsCountStatsResponse(String doctorId, String doctorName, Date date, Long newsCount) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.date = date.toLocalDate();
        this.newsCount = newsCount;
    }
}
