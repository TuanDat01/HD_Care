package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.Like;
import com.doctorcare.PD_project.entity.Post;
import com.doctorcare.PD_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, String> {
    Optional<Like> findByUserAndPost(User user, Post post);
}
