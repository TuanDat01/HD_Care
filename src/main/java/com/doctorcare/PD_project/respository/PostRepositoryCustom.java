package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> searchByMultipleKeywords(String keyword, String currentUserId, Pageable pageable);
}
