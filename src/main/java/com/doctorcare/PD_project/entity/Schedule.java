package com.doctorcare.PD_project.entity;

import com.doctorcare.PD_project.validation.ScheduleConstraint;
import com.doctorcare.PD_project.validation.TimeConstraint;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@TimeConstraint
@AllArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    
    @Column(name = "start_time")
    @ScheduleConstraint(message = "DATE_INVALID")
    LocalDateTime start;

    @Column(name = "end_time")
    LocalDateTime end;

    @Column(name = "is_available")
    boolean available;

    @Column(name = "quantity_patient")
    Integer quantityPatient;

    @Column(name = "quantity_current")
    int quantityCurrent;
}
