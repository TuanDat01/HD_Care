package com.doctorcare.PD_project.event.listen;

import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.enums.AppointmentStatus;
import com.doctorcare.PD_project.event.create.AppointmentStatusChange;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Slf4j
public class EntityListener {
    ApplicationEventPublisher applicationEventPublisher;

    @PreUpdate
    public void beforeUpdate(Appointment appointment) {
        log.info(appointment.getStatus());
        if (Objects.equals(appointment.getStatus(), AppointmentStatus.CONFIRMED.toString())) {
            applicationEventPublisher.publishEvent(new AppointmentStatusChange(appointment, false, null));
        } else if (Objects.equals(appointment.getStatus(), AppointmentStatus.CANCELLED.toString())) {
            applicationEventPublisher.publishEvent(new AppointmentStatusChange(appointment, true, appointment.getNote()));
        }

    }

}
