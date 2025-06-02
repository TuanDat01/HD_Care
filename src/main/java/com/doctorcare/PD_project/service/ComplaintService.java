package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.Complaint;
import com.doctorcare.PD_project.enums.ComplaintStatus;
import com.doctorcare.PD_project.respository.AppointmentRepository;
import com.doctorcare.PD_project.respository.ComplaintRepository;
import com.doctorcare.PD_project.respository.TransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ComplaintService {
    ComplaintRepository complaintRepository;
    VNPayService vnPayService;
    TransactionRepository transactionRepository;
    AppointmentRepository appointmentRepository;

    /**
     * Khách hàng gửi khiếu nại
     */
    public Complaint submitComplaint(String appointmentId, String reason) {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Complaint c = new Complaint();
        c.setAppointment(appt);
        c.setPatient(appt.getPatient());
        c.setDoctor(appt.getDoctor());
        c.setReason(reason);
        c.setStatus(ComplaintStatus.PENDING_REVIEW);
        c.setCreatedAt(LocalDateTime.now());
        return complaintRepository.save(c);
    }

    /**
     * Quản trị viên review khiếu nại
     */
    public Complaint reviewComplaint(String complaintId, boolean approve, String notes, String ipAddr) {
        Complaint c = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        c.setResolvedAt(LocalDateTime.now());
        c.setResolutionNotes(notes);

        if (approve) {
            boolean refunded = vnPayService.refundPayment(c.getAppointment().getId(), ipAddr);
            c.setStatus(ComplaintStatus.APPROVED_REFUND);
        } else {
            c.setStatus(ComplaintStatus.REJECTED);
        }
        return complaintRepository.save(c);
    }
}