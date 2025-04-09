package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.Comment;
import com.doctorcare.PD_project.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
    Page<Comment> findAllByPost(Post post, Pageable pageable);
}
