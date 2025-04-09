package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.Post;
import com.doctorcare.PD_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository extends JpaRepository<Post, String> {
    Page<Post> findAllByUser(User user, Pageable pageable);

    Page<Post> findAllByUserAndIsHiddenFalse(User user, Pageable pageable);

    Page<Post> findAllByIsHiddenFalse(Pageable pageable);

    Page<Post> findAllByIsHiddenFalseOrderByCountLikesDescCountCommentsDesc(Pageable pageable);
}