package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.News;
import com.doctorcare.PD_project.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, String> {
    Page<News> findByIsApprovedTrueAndIsDraftFalse(Pageable pageable);
    Page<News> findByCategoryAndIsApprovedTrueAndIsDraftFalse(String category, Pageable pageable);
    Page<News> findByIsApprovedFalseAndIsDraftFalse(Pageable pageable);  // pending
    Page<News> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword,
                                                                            String contentKeyword,
                                                                            Pageable pageable);
    default Page<News> searchByTitleOrContent(String keyword, Pageable pageable) {
        return findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
    }

    Page<News> findByAuthorAndIsDraftTrue(User doctor, Pageable pageable);

    Page<News> findByAuthorAndIsDraftFalseAndApprovedByIsNotNullAndIsApprovedTrue(User doctor, Pageable pageable);
    Page<News> findByAuthorAndIsDraftFalseAndApprovedByIsNotNullAndIsApprovedFalse(User doctor, Pageable pageable);
    Page<News> findByAuthorAndIsDraftFalseAndApprovedByIsNull(User doctor, Pageable pageable);

    Page<News> findByAssignedToAndApprovedByIsNullAndIsDraftFalse(User assignedTo, Pageable pageable);
    Page<News> findByApprovedByAndIsApprovedTrue(User approvedBy, Pageable pageable);
    Page<News> findByApprovedByAndIsApprovedFalse(User approvedBy, Pageable pageable);
    Page<News> findByAssignedToIsNullAndIsDraftFalse(Pageable pageable);
}
