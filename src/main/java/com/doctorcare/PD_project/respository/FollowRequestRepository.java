package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.FollowRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRequestRepository extends JpaRepository<FollowRequest, String> {
}