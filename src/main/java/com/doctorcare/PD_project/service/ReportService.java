package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.ReportRequest;
import com.doctorcare.PD_project.dto.response.ReportDetailResponse;
import com.doctorcare.PD_project.dto.response.ReportedPostResponse;
import com.doctorcare.PD_project.entity.Post;
import com.doctorcare.PD_project.entity.ReportImage;
import com.doctorcare.PD_project.entity.ReportPost;
import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.enums.ReportStatus;
import com.doctorcare.PD_project.enums.Roles;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.respository.PostRepository;
import com.doctorcare.PD_project.respository.ReportImageRepository;
import com.doctorcare.PD_project.respository.ReportPostRepository;
import com.doctorcare.PD_project.respository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportService {
    ReportPostRepository reportPostRepo;
    ReportImageRepository reportImageRepo;
    PostRepository postRepo;
    UserRepository userRepo;

    // Lấy userId từ JWT trong service
    private String getCurrentUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaim("id");
    }

    private boolean isAdministrator() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaim("roles").equals(Roles.ADMIN.name());
    }

    // 1. User gửi báo cáo
    @Transactional
    public void reportPost(String postId, ReportRequest dto) throws AppException {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        String userId = getCurrentUserId();  // tương tự SocialNetworkService
        User reporter = userRepo.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        ReportPost report = new ReportPost();
        report.setPost(post);
        report.setReporter(reporter);
        report.setContent(dto.getContent());
        report.setStatus(ReportStatus.NEW);
        report.setCreatedAt(LocalDateTime.now());

        if (dto.getImageUrls() != null) {
            List<ReportImage> imgs = dto.getImageUrls().stream().map(url -> {
                ReportImage ri = new ReportImage();
                ri.setImageUrl(url);
                ri.setReportPost(report);
                return ri;
            }).toList();
            report.setImages(imgs);
        }
        reportPostRepo.save(report);
    }

    // 2. Admin: lấy danh sách các post bị báo cáo
    public Page<ReportedPostResponse> listReportedPosts(int page, int size) throws AppException {
        if (!isAdministrator()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Page<Post> posts = reportPostRepo.findReportedPosts(PageRequest.of(page, size));
        return posts.map(post -> {
            long countNew = reportPostRepo.countByPostAndStatus(post, ReportStatus.NEW);
            return new ReportedPostResponse(post.getId(), post.getContent(), post.getCreatedAt(), countNew);
        });
    }

    // 3. Admin: lấy chi tiết báo cáo của một post
    public Page<ReportDetailResponse> getReportsByPost(String postId, int page, int size) throws AppException {
        if (!isAdministrator()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        return reportPostRepo.findAllByPost(post, PageRequest.of(page, size))
                .map(rp -> new ReportDetailResponse(
                        rp.getId(),
                        rp.getReporter().getId(),
                        rp.getReporter().getName(),
                        rp.getContent(),
                        rp.getImages().stream().map(ReportImage::getImageUrl).toList(),
                        rp.getStatus(),
                        rp.getCreatedAt()
                ));
    }

    // 4. Admin: duyệt (xử lý) báo cáo → xóa post và đánh dấu báo cáo RESOLVED
    @Transactional
    public void resolveReportsForPost(String postId) throws AppException {
        if (!isAdministrator()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        // đánh dấu tất cả báo cáo
        reportPostRepo.findAllByPost(post, Pageable.unpaged()).forEach(rp -> {
            rp.setStatus(ReportStatus.RESOLVED);
        });
        // xóa bài
        postRepo.delete(post);
    }

    // 5. Admin: từ chối báo cáo (giữ post)
    @Transactional
    public void rejectReport(String reportId) throws AppException {
        if (!isAdministrator()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        ReportPost rp = reportPostRepo.findById(reportId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        rp.setStatus(ReportStatus.REJECTED);
        reportPostRepo.save(rp);
    }
}
