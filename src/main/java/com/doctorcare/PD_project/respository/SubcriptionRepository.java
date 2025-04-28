package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.SubcriptionWebhook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubcriptionRepository extends JpaRepository<SubcriptionWebhook, Long> {
//    List<SubcriptionWebhook> findByUserIdAndAndEventType(Long id, String eventType);
}
