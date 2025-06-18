package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.Post;
import com.doctorcare.PD_project.entity.ReportPost;
import com.doctorcare.PD_project.enums.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportPostRepository extends JpaRepository<ReportPost, String> {
    // Lấy tất cả báo cáo theo bài viết, phân trang
    Page<ReportPost> findAllByPost(Post post, Pageable pageable);

    // Lấy tất cả các post đã có báo cáo (distinct)
    @Query("SELECT DISTINCT rp.post FROM ReportPost rp")
    Page<Post> findReportedPosts(Pageable pageable);

    // Đếm số report với trạng thái cụ thể trên một post
    long countByPostAndStatus(Post post, ReportStatus reportStatus);
}
