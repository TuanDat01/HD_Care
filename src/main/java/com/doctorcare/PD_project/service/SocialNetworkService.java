package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.annotation.EventTrigger;
import com.doctorcare.PD_project.dto.request.CreateCommentRequest;
import com.doctorcare.PD_project.dto.request.CreatePostRequest;
import com.doctorcare.PD_project.dto.request.UpdateCommentRequest;
import com.doctorcare.PD_project.dto.request.UpdatePostRequest;
import com.doctorcare.PD_project.dto.response.*;
import com.doctorcare.PD_project.entity.*;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.enums.NotificationType;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.CommentMapper;
import com.doctorcare.PD_project.mapping.PostMapper;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.respository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SocialNetworkService {

    PostRepository postRepository;
    UserRepository userRepository;
    LikeRepository likeRepository;
    CommentRepository commentRepository;
    UserSavedPostRepository userSavedPostRepository;
    FollowRequestRepository followRequestRepository;
    UserFollowRepository userFollowRepository;

    UserMapper userMapper;
    PostMapper postMapper;
    CommentMapper commentMapper;

    // Lấy userId từ JWT trong service
    private String getCurrentUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaim("id");
    }

    // **** POST **** //
    @Transactional
    public PostResponse createPost(CreatePostRequest request) throws AppException {
        String userId = getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Tạo entity Post
        Post post = postMapper.toPost(request);
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(user);

        // Xử lý ảnh
        if (request.getImages() != null) {
            List<PostImage> images = new ArrayList<>();
            for (PostImage imgReq : request.getImages()) {
                PostImage img = new PostImage();
                img.setImageUrl(imgReq.getImageUrl());
                img.setPost(post);
                images.add(img);
            }
            post.setImages(images);
        }

        postRepository.save(post);
        return toPostResponseWithFlags(post);
    }

    @Transactional
    public PostResponse updatePost(String postId, UpdatePostRequest request) throws AppException {
        String userId = getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        if (!post.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        post.setContent(request.getContent());
        post.setHidden(request.isHidden());
        post.setUpdatedAt(LocalDateTime.now());

        // Cập nhật ảnh: xóa cũ và thêm mới
        post.getImages().clear();
        if (request.getImages() != null) {
            for (PostImage imgReq : request.getImages()) {
                PostImage img = new PostImage();
                img.setImageUrl(imgReq.getImageUrl());
                img.setPost(post);
                post.getImages().add(img);
            }
        }

        postRepository.save(post);
        return toPostResponseWithFlags(post);
    }

    public PostResponse getPostById(String postId) throws AppException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        String currentUserId = getCurrentUserId();

        // Kiểm tra quyền truy cập
        if (post.isHidden()) {
            if (!post.getUser().getId().equals(currentUserId)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        } else {
            User owner = post.getUser();
            if (owner instanceof Patient && ((Patient) owner).isPrivate()) {
                Optional<UserFollow> followOpt = userFollowRepository.findByFollowerAndFollowing(
                        userRepository.findById(currentUserId)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)),
                        owner);
                if (followOpt.isEmpty()) {
                    throw new AppException(ErrorCode.UNAUTHORIZED);
                }
            }
        }

//        PostResponse response = postMapper.toPostResponse(post);
//        response.setUser(userMapper.toBasicInfoUserResponse(post.getUser()));
        return toPostResponseWithFlags(post);
    }

    public List<PostResponse> getAllPostsByUser(String targetUserId, int page, int size) throws AppException {
        String currentUserId = getCurrentUserId();
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Tạo Pageable có sort theo createdAt DESC
        Sort sortByCreatedDesc = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sortByCreatedDesc);

        Page<Post> postPage;
        if (targetUserId.equals(currentUserId)) {
            // Xem trang của chính mình: lấy tất cả bài (công khai & riêng tư)
            postPage = postRepository.findAllByUser(targetUser, pageable);
        } else {
            // Xem trang người khác: kiểm tra private/follow
            if (targetUser instanceof Patient && ((Patient) targetUser).isPrivate()) {
                User currentUser = userRepository.findById(currentUserId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
                Optional<UserFollow> followOpt = userFollowRepository
                        .findByFollowerAndFollowing(currentUser, targetUser);
                if (followOpt.isEmpty()) {
                    return Collections.emptyList();
                }
            }
            // Chỉ lấy bài công khai, đã sort ở DB
            postPage = postRepository.findAllByUserAndIsHiddenFalse(targetUser, pageable);
        }

        return postPage.stream()
                .map(p -> {
                    try {
                        return toPostResponseWithFlags(p);
                    } catch (AppException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    @Transactional
    public void deletePost(String postId) throws AppException {
        String userId = getCurrentUserId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        if (!post.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        postRepository.delete(post);
    }

    // **** NEW: Discover Posts ****
    public Page<PostResponse> discoverPosts(int page, int size) throws AppException {
        String currentUserId = getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // --- Nhóm 0: Bài viết mới nhất (công khai), sắp xếp theo createdAt desc ---
        List<Post> group0 = postRepository
                .findAllByIsHiddenFalseOrderByCreatedAtDesc(Pageable.unpaged())
                .getContent();

        // --- Nhóm 1: Bài viết từ người đang follow (công khai), sắp xếp theo createdAt desc ---
        List<User> followings = userFollowRepository.findByFollower(currentUser, Pageable.unpaged())
                .getContent().stream().map(UserFollow::getFollowing).toList();
        List<Post> group1 = followings.stream()
                .flatMap(u -> postRepository.findAllByUserAndIsHiddenFalse(u, Pageable.unpaged())
                        .getContent().stream())
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .toList();

        // --- Nhóm 2: Bài viết tương tác cao (công khai), sắp xếp theo tổng tương tác desc ---
        List<Post> group2 = postRepository
                .findAllByIsHiddenFalseOrderByCountLikesDescCountCommentsDesc(Pageable.unpaged())
                .getContent().stream()
                .filter(p -> !followings.contains(p.getUser()))
                .toList();

        // --- Nhóm 3: Các bài viết công khai còn lại ---
        List<Post> allPublic = postRepository.findAllByIsHiddenFalse(Pageable.unpaged()).getContent();
        List<Post> group3 = allPublic.stream()
                .filter(p -> !group0.contains(p)
                        && !group1.contains(p)
                        && !group2.contains(p))
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .toList();

        // --- Ghép các nhóm, loại bỏ trùng lặp ---
        List<Post> combined = new ArrayList<>();
        combined.addAll(group0);
        combined.addAll(group1.stream().filter(p -> !combined.contains(p)).toList());
        combined.addAll(group2.stream().filter(p -> !combined.contains(p)).toList());
        combined.addAll(group3.stream().filter(p -> !combined.contains(p)).toList());

        // --- Phân trang thủ công với guard (không giới hạn combined trước) ---
        int total = combined.size();
        int start = page * size;
        if (start >= total) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), total);
        }
        int end = Math.min(start + size, total);

        List<PostResponse> content = combined.subList(start, end).stream()
                .map(p -> {
                    try {
                        return toPostResponseWithFlags(p);
                    } catch (AppException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }

    // **** NEW: Get Latest Posts ****
    public Page<PostResponse> getLatestPosts(int page, int size) throws AppException {
        List<Post> allPublicPosts = postRepository
                .findAllByIsHiddenFalseOrderByCreatedAtDesc(Pageable.unpaged())
                .getContent();

        int total = allPublicPosts.size();
        int start = page * size;
        if (start >= total) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), total);
        }
        int end = Math.min(start + size, total);

        List<PostResponse> result = allPublicPosts.subList(start, end).stream()
                .map(post -> {
                    try {
                        return toPostResponseWithFlags(post);
                    } catch (AppException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        return new PageImpl<>(result, PageRequest.of(page, size), total);
    }

    // **** NEW: Posts From Following ****
    public Page<PostResponse> getPostsFromFollowing(int page, int size) throws AppException {
        String currentUserId = getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 1. Lấy danh sách những người mà currentUser đang follow
        List<UserFollow> allFollowing = userFollowRepository
                .findByFollower(currentUser, Pageable.unpaged())
                .getContent();

        // 2. Lấy tất cả bài viết công khai của họ
        List<Post> allPosts = allFollowing.stream()
                // Thay f.getFollower() bằng f.getFollowing()
                .flatMap(f -> postRepository
                        .findAllByUserAndIsHiddenFalse(f.getFollowing(), Pageable.unpaged())
                        .getContent().stream())
                // Sắp xếp giảm dần theo thời gian tạo
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .toList();

        // 3. Phân trang thủ công với guard
        int total = allPosts.size();
        int start = page * size;
        if (start >= total) {
            // Nếu bắt đầu vượt quá tổng số phần tử, trả về trang rỗng
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), total);
        }
        int end = Math.min(start + size, total);

        // 4. Chuyển thành PostResponse (có cả liked & saved)
        List<PostResponse> result = allPosts.subList(start, end).stream()
                .map(post -> {
                    try {
                        return toPostResponseWithFlags(post);
                    } catch (AppException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        // 5. Trả về PageImpl với đúng pageable và total
        return new PageImpl<>(result, PageRequest.of(page, size), total);
    }

    // **** LIKE **** //
    @EventTrigger(event = NotificationType.LIKE_POST)
    public Post interactPost(String postId) throws AppException {
        String userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Optional<Like> existingLikeOpt = likeRepository.findByUserAndPost(user, post);

        if (existingLikeOpt.isPresent()) {
            Like existingLike = existingLikeOpt.get();

            post.getLikes().remove(existingLike);
            post.setCountLikes(post.getCountLikes() - 1);

            postRepository.save(post);
            likeRepository.delete(existingLike);
        } else {
            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setPost(post);
            newLike.setStatus(true);

            likeRepository.save(newLike);
            post.getLikes().add(newLike);

            post.setCountLikes(post.getCountLikes() + 1);
            postRepository.save(post);
            return post;
        }

        return null;
    }

    // **** COMMENT **** //
    @EventTrigger(event = NotificationType.COMMENT_POST)
    public CommentResponse commentOnPost(CreateCommentRequest request, String postId) throws AppException {
        String userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Comment comment = commentMapper.toComment(request);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);
        post.getComments().add(comment);

        post.setCountComments(post.getCountComments() + 1);
        postRepository.save(post);

        return commentMapper.toCommentResponse(comment);
    }

    public List<CommentResponse> getAllCommentsByPost(String postId, int page, int size) throws AppException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Page<Comment> commentPage = commentRepository.findAllByPost(post, PageRequest.of(page, size));

        return commentPage.stream().map(commentMapper::toCommentResponse).toList();
    }

    public void deleteComment(String commentId) throws AppException {
        String userId = getCurrentUserId();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        commentRepository.delete(comment);
    }

    public CommentResponse updateComment(String commentId, UpdateCommentRequest request) throws AppException {
        String userId = getCurrentUserId();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        comment.setContent(request.getContent());
        commentRepository.save(comment);

        return commentMapper.toCommentResponse(comment);
    }

    // **** SAVE POST **** //
    public void toggleSave(String postId) throws AppException {
        String userId = getCurrentUserId();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Optional<UserSavedPost> existing = userSavedPostRepository.findByUserAndPost(user, post);

        if (existing.isPresent()) {
            userSavedPostRepository.delete(existing.get());
        } else {
            UserSavedPost savedNews = UserSavedPost.builder()
                    .user(user)
                    .post(post)
                    .build();
            userSavedPostRepository.save(savedNews);
        }
    }

    public List<PostResponse> getAllSavedPosts(int page, int size) throws AppException {
        String userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Page<UserSavedPost> savedPage = userSavedPostRepository
                .findAllByUser(user, PageRequest.of(page, size));

        return savedPage.stream().map(UserSavedPost::getPost)
                .map(postMapper::toPostResponse)
                .toList();
    }

    // **** FOLLOW **** //
    public void followUser(String targetUserId) throws AppException {
        String userId = getCurrentUserId();

        if (userId.equals(targetUserId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Optional<UserFollow> existingFollow = userFollowRepository
                .findByFollowerAndFollowing(user, targetUser);

        if (existingFollow.isPresent()) {
            userFollowRepository.delete(existingFollow.get());
            user.setFollowingCount(user.getFollowingCount() - 1);
            targetUser.setFollowersCount(targetUser.getFollowersCount() - 1);

            userRepository.save(user);
            userRepository.save(targetUser);
            return;
        }
        if (targetUser instanceof Patient patient && patient.isPrivate()) {
            boolean alreadyRequested = patient.getFollowRequests().stream()
                    .anyMatch(fr -> fr.getFollower().equals(user));

            if (alreadyRequested) {
                throw new AppException(ErrorCode.FOLLOW_REQUEST_EXIST);
            }

            FollowRequest followRequest = new FollowRequest();
            followRequest.setUser(patient);
            followRequest.setFollower(user);

            followRequestRepository.save(followRequest);
            patient.setFollowRequestCount(patient.getFollowRequestCount() + 1);

            patient.getFollowRequests().add(followRequest);
            userRepository.save(patient);
            return;
        }

        UserFollow userFollow = new UserFollow();
        userFollow.setFollower(user);
        userFollow.setFollowing(targetUser);
        userFollowRepository.save(userFollow);

        user.setFollowingCount(user.getFollowingCount() + 1);
        targetUser.setFollowersCount(targetUser.getFollowersCount() + 1);
        userRepository.save(user);
        userRepository.save(targetUser);
    }

    // **** NEW: Suggest Users To Follow (no duplicates across pages) ****
    public Page<BasicInfoUserResponse> suggestUsersToFollow(int page, int size) throws AppException {
        String currentUserId = getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 1. Lấy tất cả user (trừ chính mình)
        List<User> allUsers = userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(currentUserId))
                .toList();

        // 2. Lọc ra những user đã follow
        List<User> alreadyFollowed = userFollowRepository.findByFollower(currentUser, Pageable.unpaged())
                .getContent().stream()
                .map(UserFollow::getFollowing)
                .toList();

        // 3. Chỉ giữ lại user chưa follow
        List<User> notFollowed = allUsers.stream()
                .filter(u -> !alreadyFollowed.contains(u))
                .toList();

        // 4. Tạo seed từ currentUserId
        int seed = currentUserId.hashCode();

        // 5. Sort theo key = userId.hashCode() XOR seed
        List<User> sorted = notFollowed.stream()
                .sorted(Comparator
                        .comparingInt((User u) -> u.getId().hashCode() ^ seed)
                        // nếu key trùng, break tie bằng chính ID để ổn định
                        .thenComparing(User::getId))
                .toList();

        // 6. Phân trang thủ công
        int total = sorted.size();
        int start = page * size;
        if (start >= total) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), total);
        }
        int end = Math.min(start + size, total);

        List<BasicInfoUserResponse> content = sorted.subList(start, end).stream()
                .map(userMapper::toBasicInfoUserResponse)
                .toList();

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }

    // **** UPDATED: getAllFollowingUsers ****
    public List<BasicInfoUserResponse> getAllFollowingUsers(String targetUserId, int page, int size) throws AppException {
        String currentUserId = getCurrentUserId();
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Quyền truy cập
        if (!targetUserId.equals(currentUserId)
                && targetUser instanceof Patient patient
                && patient.isPrivate()) {
            User currentUser = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            Optional<UserFollow> followOpt = userFollowRepository
                    .findByFollowerAndFollowing(currentUser, targetUser);
            if (followOpt.isEmpty()) {
                return Collections.emptyList();
            }
        }

        // Phân trang
        Page<UserFollow> pageResult = userFollowRepository
                .findByFollower(targetUser, PageRequest.of(page, size));

        // Load set các ID mà currentUser đang follow
        Set<String> currentFollowingIds = loadCurrentUserFollowingIds();

        // Ánh xạ và set flag
        return pageResult.stream()
                .map(uf -> {
                    BasicInfoUserResponse resp = userMapper.toBasicInfoUserResponse(uf.getFollowing());
                    resp.setFollowed(currentFollowingIds.contains(resp.getId()));
                    return resp;
                })
                .toList();
    }

    // **** UPDATED: getAllFollowers ****
    public List<BasicInfoUserResponse> getAllFollowers(String targetUserId, int page, int size) throws AppException {
        String currentUserId = getCurrentUserId();
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Quyền truy cập
        if (!targetUserId.equals(currentUserId)
                && targetUser instanceof Patient patient
                && patient.isPrivate()) {
            User currentUser = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            Optional<UserFollow> followOpt = userFollowRepository
                    .findByFollowerAndFollowing(currentUser, targetUser);
            if (followOpt.isEmpty()) {
                return Collections.emptyList();
            }
        }

        // Phân trang
        Page<UserFollow> pageResult = userFollowRepository
                .findByFollowing(targetUser, PageRequest.of(page, size));

        // Load set các ID mà currentUser đang follow
        Set<String> currentFollowingIds = loadCurrentUserFollowingIds();

        // Ánh xạ và set flag
        return pageResult.stream()
                .map(uf -> {
                    BasicInfoUserResponse resp = userMapper.toBasicInfoUserResponse(uf.getFollower());
                    resp.setFollowed(currentFollowingIds.contains(resp.getId()));
                    return resp;
                })
                .toList();
    }

    public void acceptFollowRequest(String followRequestId) throws AppException {
        String userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        FollowRequest followRequest = followRequestRepository.findById(followRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        if (!followRequest.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        User follower = followRequest.getFollower();
        UserFollow userFollow = new UserFollow();

        userFollow.setFollower(follower);
        userFollow.setFollowing(user);
        userFollowRepository.save(userFollow);

        user.setFollowersCount(user.getFollowersCount() + 1);
        follower.setFollowingCount(follower.getFollowingCount() + 1);

        if (user instanceof Patient patient) {
            patient.getFollowRequests().remove(followRequest);
            patient.setFollowRequestCount(patient.getFollowRequestCount() - 1);
            userRepository.save(patient);
        } else {
            userRepository.save(user);
        }

        userRepository.save(follower);
        followRequestRepository.delete(followRequest);
    }

    public void rejectFollowRequest(String followRequestId) throws AppException {
        String userId = getCurrentUserId();

        userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        FollowRequest followRequest = followRequestRepository.findById(followRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        if (!followRequest.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        User patientUser = followRequest.getUser();

        if (patientUser instanceof Patient patient) {
            patient.getFollowRequests().remove(followRequest);
            patient.setFollowRequestCount(patient.getFollowRequestCount() - 1);
            userRepository.save(patient);
        }

        followRequestRepository.delete(followRequest);
    }

    public List<BasicInfoUserResponse> getAllFollowRequests(int page, int size) throws AppException {
        String userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!(user instanceof Patient patient)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Sử dụng stream với skip và limit cho phân trang
        return patient.getFollowRequests().stream()
                .skip((long) page * size)
                .limit(size)
                .map(fr -> userMapper.toBasicInfoUserResponse(fr.getFollower()))
                .toList();
    }

    public void setPrivate() throws AppException {
        String userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!(user instanceof Patient patient)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        patient.setPrivate(!patient.isPrivate());
        userRepository.save(patient);
    }

    /**
     * Ánh xạ Post → PostResponse, kèm flags liked & saved
     */
    private PostResponse toPostResponseWithFlags(Post post) throws AppException {
        PostResponse res = postMapper.toPostResponse(post);
        res.setUser(userMapper.toBasicInfoUserResponse(post.getUser()));

        // Lấy current user
        String currentUserId = getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Kiểm tra liked
        boolean liked = likeRepository.findByUserAndPost(currentUser, post).isPresent();
        res.setLiked(liked);

        // Kiểm tra saved
        boolean saved = userSavedPostRepository.findByUserAndPost(currentUser, post).isPresent();
        res.setSaved(saved);

        return res;
    }

    // trong class SocialNetworkService, trước các method
    private Set<String> loadCurrentUserFollowingIds() throws AppException {
        String currentUserId = getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        // Lấy tất cả quan hệ follow của currentUser (unpaged)
        List<UserFollow> ufList = userFollowRepository.findByFollower(currentUser, Pageable.unpaged()).getContent();
        return ufList.stream()
                .map(uf -> uf.getFollowing().getId())
                .collect(Collectors.toSet());
    }
}