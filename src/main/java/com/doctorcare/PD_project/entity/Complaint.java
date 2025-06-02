package com.doctorcare.PD_project.entity;

import com.doctorcare.PD_project.enums.ComplaintStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne @JoinColumn(name="appointment_id", nullable=false)
    Appointment appointment;

    @ManyToOne @JoinColumn(name="patient_id", nullable=false)
    Patient patient;

    @ManyToOne @JoinColumn(name="doctor_id", nullable=false)
    Doctor doctor;

    String reason;                // Lý do khiếu nại do khách hàng nhập
    @Enumerated(EnumType.STRING)
    ComplaintStatus status;

    LocalDateTime createdAt;
    LocalDateTime resolvedAt;
    String resolutionNotes;       // Ghi chú quyết định
}
