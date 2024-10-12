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
public class MedicineDetail  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    String id;

    String name;

    String instruction;

    String medicineType;

    String quantity;




}
