package com.doctorcare.PD_project.event.create;

import com.doctorcare.PD_project.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OnRegisterEvent {
    User user;
    String appUrl;
    Locale locale;

}
