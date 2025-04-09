package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.Post;
import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.entity.UserSavedPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSavedPostRepository extends JpaRepository<UserSavedPost, String> {
    Page<UserSavedPost> findAllByUser(User user, Pageable pageable);
    Optional<UserSavedPost> findByUserAndPost(User user, Post post);
}
