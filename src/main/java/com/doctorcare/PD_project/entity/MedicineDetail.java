package com.doctorcare.PD_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicineDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;
    String instruction;
}
