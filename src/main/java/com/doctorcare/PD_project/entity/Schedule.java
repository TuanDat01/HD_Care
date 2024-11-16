package com.doctorcare.PD_project.entity;

import com.doctorcare.PD_project.validation.ScheduleConstraint;
import com.doctorcare.PD_project.validation.TimeConstraint;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@TimeConstraint
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
}
