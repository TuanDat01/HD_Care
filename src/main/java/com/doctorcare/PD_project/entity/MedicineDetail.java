package com.doctorcare.PD_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.UUID)

    String id;

    String name;

    String instruction;

    String medicineType;

    String quantity;
    String note;
    @ManyToOne
    @JoinColumn(name = "prescription_id")
    @JsonIgnore
    Prescription prescription;

    @Override
    public String toString() {
        return "MedicineDetail{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", instruction='" + instruction + '\'' +
                ", medicineType='" + medicineType + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
