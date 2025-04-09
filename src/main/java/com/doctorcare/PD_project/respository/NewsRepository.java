package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, String> {

    Page<News> findByIsApprovedTrueAndIsDraftFalse(Pageable pageable);

    Page<News> findByCategoryAndIsApprovedTrueAndIsDraftFalse(String category, Pageable pageable);

    Page<News> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword, Pageable pageable);

    default Page<News> searchByTitleOrContent(String keyword, Pageable pageable) {
        return findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
    }
}
