package com.doctorcare.PD_project.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Patient extends User{

    String address;

    @OneToMany
    @JoinColumn(name = "appointment_id")
    List<Appointment> appointments;

}
