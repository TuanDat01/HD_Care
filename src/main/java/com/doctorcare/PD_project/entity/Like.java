package com.doctorcare.PD_project.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "like_status")
@DiscriminatorColumn(name = "type")
public class Like  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    boolean status;
}
