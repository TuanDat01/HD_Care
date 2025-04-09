package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.entity.UserFollow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollow, String> {
    Optional<UserFollow> findByFollowerAndFollowing(User follower, User following);
    Page<UserFollow> findByFollower(User follower, Pageable pageable);
    Page<UserFollow> findByFollowing(User following, Pageable pageable);
}
