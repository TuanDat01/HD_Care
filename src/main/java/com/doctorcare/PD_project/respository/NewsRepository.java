package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.News;
import com.doctorcare.PD_project.entity.User;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, String>,NewsRepositoryCustom {
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

    @Query("SELECT d.id, d.name, FUNCTION('DATE', n.createdAt), COUNT(n) " +
            "FROM News n JOIN n.author d " +
            "WHERE n.isApproved = true AND n.isDraft = false " +
            "AND (:doctorId IS NULL OR d.id = :doctorId) " +
            "AND (:from IS NULL OR FUNCTION('DATE', n.createdAt) >= :from) " +
            "AND (:to IS NULL OR FUNCTION('DATE', n.createdAt) <= :to) " +
            "GROUP BY d.id, d.name, FUNCTION('DATE', n.createdAt) " +
            "ORDER BY FUNCTION('DATE', n.createdAt) DESC")
    List<Object[]> statsNewsCount(@Param("doctorId") String doctorId,
                                  @Param("from") Date from,
                                  @Param("to") Date to);

    @Query("SELECT n.id, n.title, SUM(CASE WHEN i.isUseful = true THEN 1 ELSE 0 END) " +
            "FROM News n LEFT JOIN n.interactions i " +
            "WHERE n.isApproved = true AND n.isDraft = false " +
            "AND (:from IS NULL OR FUNCTION('DATE', n.createdAt) >= :from) " +
            "AND (:to IS NULL OR FUNCTION('DATE', n.createdAt) <= :to) " +
            "GROUP BY n.id, n.title " +
            "ORDER BY SUM(CASE WHEN i.isUseful = true THEN 1 ELSE 0 END) DESC")
    List<Object[]> statsTopFavorites(@Param("from") Date from,
                                     @Param("to") Date to,
                                     Pageable pageable);
}
