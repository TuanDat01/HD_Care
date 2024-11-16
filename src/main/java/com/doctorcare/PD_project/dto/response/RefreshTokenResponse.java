package com.doctorcare.PD_project.dto.response;

import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Prescription;
import com.doctorcare.PD_project.entity.Schedule;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenResponse {
    String accessToken;
    String refreshToken;
;

}
