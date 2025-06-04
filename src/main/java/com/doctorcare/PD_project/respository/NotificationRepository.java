package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.Notification;
import com.doctorcare.PD_project.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    Page<Notification> findAllByReceiverUsernameOrderByCreatedAtDesc(String username, Pageable pageable);

    List<Notification> findAllByReceiverUsernameOrderByCreatedAtDesc(String username);

    Page<Notification> findAllByReceiverOrderByCreatedAtDesc(User user, Pageable pageable);
}
