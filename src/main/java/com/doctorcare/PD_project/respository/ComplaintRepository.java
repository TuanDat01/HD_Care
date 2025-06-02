package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.Complaint;
import com.doctorcare.PD_project.enums.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint,String> {
    List<Complaint> findByStatus(ComplaintStatus status);
}

