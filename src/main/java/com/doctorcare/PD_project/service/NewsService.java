package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.NewsCreateRequest;
import com.doctorcare.PD_project.dto.request.NewsUpdateRequest;
import com.doctorcare.PD_project.dto.response.NewsResponse;
import com.doctorcare.PD_project.entity.News;
import com.doctorcare.PD_project.entity.NewsInteraction;
import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.entity.UserSavedNews;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.enums.Roles;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.NewsMapper;
import com.doctorcare.PD_project.respository.NewsRepository;
import com.doctorcare.PD_project.respository.UserRepository;
import com.doctorcare.PD_project.respository.UserSavedNewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NewsService {

    NewsRepository newsRepository;
    UserRepository userRepository;
    UserSavedNewsRepository userSavedNewsRepository;

    NewsMapper newsMapper;

    // Lấy userId từ JWT trong service
    private String getCurrentUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaim("id");
    }

    private boolean isAdministrator() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaim("roles").equals(Roles.ADMIN.name());
    }

    // Tạo tin tức mới
    public NewsResponse createNews(NewsCreateRequest newsCreateRequest) throws AppException {
        User author = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        News news = newsMapper.toNews(newsCreateRequest);
        news.setAuthor(author);
        news.setDraft(newsCreateRequest.isDraft());
        news.setCreatedAt(LocalDateTime.now());

        newsRepository.save(news);

        return newsMapper.toNewsResponse(news);
    }

    // Lấy tin tức theo ID
    public NewsResponse getNewsById(String newsId) throws AppException {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));

        return newsMapper.toNewsResponse(news);
    }

    // Lấy tất cả tin tức với phân trang (chỉ lấy tin đã duyệt và không phải tin nháp)
    public List<NewsResponse> getAllNews(int page, int size) throws AppException {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<News> newsPage = newsRepository
                .findByIsApprovedTrueAndIsDraftFalse(pageRequest);

        return newsPage.stream().map(newsMapper::toNewsResponse).toList();
    }

    // Lấy tin tức mới nhất
    public List<NewsResponse> getNewestNews(int limit) throws AppException {
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by("createdAt").descending());

        Page<News> newsPage = newsRepository
                .findByIsApprovedTrueAndIsDraftFalse(pageRequest);

        return newsPage.stream().map(newsMapper::toNewsResponse).toList();
    }

    // Lấy tin tức nổi bật (theo lượt interactUseful)
    public List<NewsResponse> getFeaturedNews(int limit) throws AppException {
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by("interactUseful").descending());

        Page<News> newsPage = newsRepository
                .findByIsApprovedTrueAndIsDraftFalse(pageRequest);

        return newsPage.stream().map(newsMapper::toNewsResponse).toList();
    }

    // Lấy tin tức theo danh mục với phân trang
    public List<NewsResponse> getNewsByCategory(String category, int page, int size) throws AppException {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<News> newsPage = newsRepository
                .findByCategoryAndIsApprovedTrueAndIsDraftFalse(category, pageRequest);

        return newsPage.stream().map(newsMapper::toNewsResponse).toList();
    }

    // Tìm tin tức theo từ khóa (tiêu đề hoặc nội dung) với phân trang
    public List<NewsResponse> searchNews(String keyword, int page, int size) throws AppException {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<News> newsPage = newsRepository.searchByTitleOrContent(keyword, pageRequest);

        return newsPage.stream().map(newsMapper::toNewsResponse).toList();
    }

    // Cập nhật tin tức (dành cho tin nháp)
    public NewsResponse updateNews(String newsId, NewsUpdateRequest newsUpdateRequest) throws AppException {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));

        if (!news.getAuthor().getId().equals(getCurrentUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        news.setTitle(newsUpdateRequest.getTitle());
        news.setContent(newsUpdateRequest.getContent());
        news.setCategory(newsUpdateRequest.getCategory());
        newsRepository.save(news);

        return newsMapper.toNewsResponse(news);
    }

    // Xóa tin tức
    public void deleteNews(String newsId) throws AppException {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));

        if (!isAdministrator() && !news.getAuthor().getId().equals(getCurrentUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        newsRepository.delete(news);
    }

    // Duyệt tin tức (admin hoặc bác sĩ)
    public NewsResponse approveNews(String newsId, boolean approve) throws AppException {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));

        if (!isAdministrator() && !news.getAssignedTo().getId().equals(getCurrentUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        news.setApproved(approve);
        newsRepository.save(news);

        return newsMapper.toNewsResponse(news);
    }

    // Phân công tin tức cho bác sĩ duyệt (dành cho admin)
    public NewsResponse assignNewsToDoctor(String newsId, String doctorId) throws AppException {
        if (isAdministrator()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));

        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        news.setAssignedTo(doctor);
        newsRepository.save(news);

        return newsMapper.toNewsResponse(news);
    }

    // Tương tác với tin tức: tăng lượt useful hoặc useless
    public void interactWithNews(String newsId, boolean isUseful) throws AppException {
        String currentUserId = getCurrentUserId();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));

        // Đảm bảo danh sách interactions không null
        if (news.getInteractions() == null) {
            news.setInteractions(new ArrayList<>());
        }

        Optional<NewsInteraction> existingInteractionOpt = news.getInteractions().stream()
                .filter(interaction -> interaction.getUser().equals(user))
                .findFirst();

        if (existingInteractionOpt.isPresent()) {
            NewsInteraction existingInteraction = existingInteractionOpt.get();
            if (existingInteraction.isUseful() == isUseful) {
                // Nếu trạng thái tương tác trùng nhau: xóa tương tác (toggle off)
                news.getInteractions().remove(existingInteraction);
                if (isUseful) {
                    news.setInteractUseful(news.getInteractUseful() - 1);
                } else {
                    news.setInteractUseless(news.getInteractUseless() - 1);
                }
            } else {
                // Nếu khác, cập nhật trạng thái tương tác và điều chỉnh số lượt tương tác
                if (isUseful) {
                    // Từ "không hữu ích" chuyển thành "hữu ích"
                    news.setInteractUseless(news.getInteractUseless() - 1);
                    news.setInteractUseful(news.getInteractUseful() + 1);
                } else {
                    // Từ "hữu ích" chuyển thành "không hữu ích"
                    news.setInteractUseful(news.getInteractUseful() - 1);
                    news.setInteractUseless(news.getInteractUseless() + 1);
                }
                existingInteraction.setUseful(isUseful);
            }
        } else {
            // Nếu chưa tương tác, tạo mới bản ghi tương tác
            NewsInteraction newInteraction = new NewsInteraction();
            newInteraction.setUser(user);
            newInteraction.setNews(news);
            newInteraction.setUseful(isUseful);
            news.getInteractions().add(newInteraction);

            if (isUseful) {
                news.setInteractUseful(news.getInteractUseful() + 1);
            } else {
                news.setInteractUseless(news.getInteractUseless() + 1);
            }
        }
        newsRepository.save(news);
    }

    // Lưu/Xóa tin tức trong mục yêu thích
    public void saveNewsToFavorite(String newsId) throws AppException {
        String userId = getCurrentUserId();

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean exists = userSavedNewsRepository.existsByUserAndNews(user, news);

        if (!exists) {
            UserSavedNews savedNews = UserSavedNews.builder()
                    .user(user)
                    .news(news)
                    .savedAt(LocalDateTime.now())
                    .build();
            userSavedNewsRepository.save(savedNews);
        } else {
            UserSavedNews savedNews = userSavedNewsRepository.findByUserAndNews(user, news)
                    .orElseThrow(() -> new AppException(ErrorCode.SAVED_NEWS_NOT_FOUND));

            userSavedNewsRepository.delete(savedNews);
        }
    }

    // Lấy danh sách tin tức đã lưu vào mục yêu thích của user với phân trang
    public List<NewsResponse> getAllFavoriteNews(int page, int size) throws AppException {
        String userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Page<UserSavedNews> savedPage = userSavedNewsRepository.findAllByUser(user, PageRequest.of(page, size));

        return savedPage.stream()
                .map(UserSavedNews::getNews)
                .map(newsMapper::toNewsResponse)
                .toList();
    }
}
