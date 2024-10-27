package com.doctorcare.PD_project.entity;

import com.doctorcare.PD_project.validation.ScheduleConstraint;
import com.doctorcare.PD_project.validation.TimeConstraint;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Data
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
