package com.doctorcare.PD_project.dto.request;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class NotificationMessage {

    private String event_type;
    private String message;
    private String username;
    private Object data;
    private LocalDateTime time;
}
