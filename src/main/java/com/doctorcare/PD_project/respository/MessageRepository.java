package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.HistoryMessage;
import com.doctorcare.PD_project.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<HistoryMessage, String> {
    Page<HistoryMessage> findHistoryMessageBySenderUsernameAndReceiverId(Pageable pageable, String sender, String receiver);

    long countBySenderUsernameAndReceiverId(String sender, String receiver);
}
