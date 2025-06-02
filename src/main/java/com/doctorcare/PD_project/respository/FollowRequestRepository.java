package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.FollowRequest;
import com.doctorcare.PD_project.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRequestRepository extends JpaRepository<FollowRequest, String> {
    Optional<FollowRequest> findByUserAndFollower(User user, User follower);

    Page<FollowRequest> findByFollower(User currentUser, Pageable page);

    Optional<Object> countByFollower(User follower);
}