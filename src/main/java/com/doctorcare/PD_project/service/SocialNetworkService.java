package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.CreateCommentRequest;
import com.doctorcare.PD_project.dto.request.CreatePostRequest;
import com.doctorcare.PD_project.dto.request.UpdateCommentRequest;
import com.doctorcare.PD_project.dto.request.UpdatePostRequest;
import com.doctorcare.PD_project.dto.response.*;
import com.doctorcare.PD_project.entity.*;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.CommentMapper;
import com.doctorcare.PD_project.mapping.PostMapper;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.respository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        return postMapper.toPostResponse(post);
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
        return postMapper.toPostResponse(post);
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

        PostResponse response = postMapper.toPostResponse(post);
        response.setUser(userMapper.toBasicInfoUserResponse(post.getUser()));
        return response;
    }

    public List<PostResponse> getAllPostsByUser(String targetUserId, int page, int size) throws AppException {
        String currentUserId = getCurrentUserId();
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Page<Post> postPage;
        if (targetUserId.equals(currentUserId)) {
            postPage = postRepository.findAllByUser(targetUser, PageRequest.of(page, size));
        } else {
            if (targetUser instanceof Patient && ((Patient) targetUser).isPrivate()) {
                User currentUser = userRepository.findById(currentUserId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
                Optional<UserFollow> followOpt = userFollowRepository.findByFollowerAndFollowing(currentUser, targetUser);
                if (followOpt.isEmpty()) {
                    return Collections.emptyList();
                }
            }
            postPage = postRepository.findAllByUserAndIsHiddenFalse(targetUser, PageRequest.of(page, size));
        }

        return postPage.stream().map(p -> {
            PostResponse res = postMapper.toPostResponse(p);
            res.setUser(userMapper.toBasicInfoUserResponse(p.getUser()));
            return res;
        }).collect(Collectors.toList());
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

        // 1. Bài viết từ người follow
        List<User> followings = userFollowRepository.findByFollower(currentUser, Pageable.unpaged())
                .getContent().stream().map(UserFollow::getFollowing).toList();
        List<Post> group1 = new ArrayList<>();
        for (User u : followings) {
            group1.addAll(postRepository.findAllByUserAndIsHiddenFalse(u, Pageable.unpaged()).getContent());
        }

        // 2. Bài viết tương tác cao
        List<Post> group2 = postRepository
                .findAllByIsHiddenFalseOrderByCountLikesDescCountCommentsDesc(PageRequest.of(0, size))
                .getContent().stream()
                .filter(p -> !followings.contains(p.getUser()))
                .toList();

        // 3. Các bài viết còn lại
        List<Post> group3 = postRepository.findAllByIsHiddenFalse(Pageable.unpaged())
                .getContent().stream()
                .filter(p -> !followings.contains(p.getUser()) && !group2.contains(p))
                .toList();

        // Kết hợp theo thứ tự ưu tiên
        List<Post> combined = new ArrayList<>(group1);
        if (combined.size() < size) {
            for (Post p : group2) {
                if (combined.size() >= size) break;
                combined.add(p);
            }
        }
        if (combined.size() < size) {
            for (Post p : group3) {
                if (combined.size() >= size) break;
                combined.add(p);
            }
        }

        // Sắp xếp theo thời gian
        combined.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        // Phân trang thủ công
        int start = page * size;
        int end = Math.min(start + size, combined.size());
        List<PostResponse> result = combined.subList(start, end).stream().map(p -> {
            PostResponse r = postMapper.toPostResponse(p);
            r.setUser(userMapper.toBasicInfoUserResponse(p.getUser()));
            return r;
        }).collect(Collectors.toList());

        return new PageImpl<>(result, PageRequest.of(page, size), combined.size());
    }

    // **** NEW: Get Latest Posts ****
    public Page<PostResponse> getLatestPosts(int page, int size) throws AppException {
        // Tái sử dụng discover logic nhưng chỉ sort theo thời gian toàn cục
        Page<PostResponse> discovered = discoverPosts(page, size);
        List<PostResponse> sorted = discovered.getContent().stream()
                .sorted(Comparator.comparing(PostResponse::getCreatedAt).reversed())
                .collect(Collectors.toList());
        return new PageImpl<>(sorted, discovered.getPageable(), discovered.getTotalElements());
    }

    // **** NEW: Posts From Followers ****
    public Page<PostResponse> getPostsFromFollowers(int page, int size) throws AppException {
        String currentUserId = getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Lấy danh sách followers
        Page<UserFollow> followersPage = userFollowRepository.findByFollowing(currentUser, PageRequest.of(page, size));
        List<Post> allPosts = new ArrayList<>();
        for (UserFollow uf : followersPage.getContent()) {
            allPosts.addAll(
                    postRepository.findAllByUserAndIsHiddenFalse(uf.getFollower(), Pageable.unpaged()).getContent()
            );
        }
        allPosts.sort(Comparator.comparing(Post::getCreatedAt).reversed());

        // Phân trang thủ công
        int start = page * size;
        int end = Math.min(start + size, allPosts.size());
        List<PostResponse> result = allPosts.subList(start, end).stream().map(p -> {
            PostResponse r = postMapper.toPostResponse(p);
            r.setUser(userMapper.toBasicInfoUserResponse(p.getUser()));
            return r;
        }).collect(Collectors.toList());

        return new PageImpl<>(result, PageRequest.of(page, size), allPosts.size());
    }

    // **** LIKE **** //
    public void interactPost(String postId) throws AppException {
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
        }
    }

    // **** COMMENT **** //
    public CommentResponse commentOnPost(CreateCommentRequest request) throws AppException {
        String userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(request.getPostId())
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

    // **** NEW: Suggest Users To Follow ****
    public Page<BasicInfoUserResponse> suggestUsersToFollow(int page, int size) throws AppException {
        String currentUserId = getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<User> allUsers = userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(currentUserId))
                .toList();
        List<User> alreadyFollowed = userFollowRepository.findByFollower(currentUser, Pageable.unpaged())
                .getContent().stream().map(UserFollow::getFollowing).toList();
        List<User> notFollowed = allUsers.stream()
                .filter(u -> !alreadyFollowed.contains(u))
                .collect(Collectors.toList());
        Collections.shuffle(notFollowed);

        int start = page * size;
        int end = Math.min(start + size, notFollowed.size());
        List<BasicInfoUserResponse> result = notFollowed.subList(start, end).stream()
                .map(userMapper::toBasicInfoUserResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(result, PageRequest.of(page, size), notFollowed.size());
    }

    // **** UPDATED: getAllFollowingUsers ****
    public List<BasicInfoUserResponse> getAllFollowingUsers(String targetUserId, int page, int size) throws AppException {
        String currentUserId = getCurrentUserId();
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!targetUserId.equals(currentUserId) && targetUser instanceof Patient && ((Patient) targetUser).isPrivate()) {
            Optional<UserFollow> followOpt = userFollowRepository.findByFollowerAndFollowing(
                    userRepository.findById(currentUserId)
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)),
                    targetUser);
            if (followOpt.isEmpty()) {
                return Collections.emptyList();
            }
        }
        Page<UserFollow> pageResult = userFollowRepository.findByFollower(targetUser, PageRequest.of(page, size));
        return pageResult.stream()
                .map(uf -> userMapper.toBasicInfoUserResponse(uf.getFollowing()))
                .collect(Collectors.toList());
    }

    // **** UPDATED: getAllFollowers ****
    public List<BasicInfoUserResponse> getAllFollowers(String targetUserId, int page, int size) throws AppException {
        String currentUserId = getCurrentUserId();
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!targetUserId.equals(currentUserId) && targetUser instanceof Patient && ((Patient) targetUser).isPrivate()) {
            Optional<UserFollow> followOpt = userFollowRepository.findByFollowerAndFollowing(
                    userRepository.findById(currentUserId)
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)),
                    targetUser);
            if (followOpt.isEmpty()) {
                return Collections.emptyList();
            }
        }
        Page<UserFollow> pageResult = userFollowRepository.findByFollowing(targetUser, PageRequest.of(page, size));
        return pageResult.stream()
                .map(uf -> userMapper.toBasicInfoUserResponse(uf.getFollower()))
                .collect(Collectors.toList());
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
}