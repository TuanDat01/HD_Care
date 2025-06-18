package com.doctorcare.PD_project.dto.response;

import com.doctorcare.PD_project.enums.ReportStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportDetailResponse {
    String reportId;
    String reporterId;
    String reporterName;
    String content;
    List<String> imageUrls;
    ReportStatus status;
    LocalDateTime createdAt;
}
