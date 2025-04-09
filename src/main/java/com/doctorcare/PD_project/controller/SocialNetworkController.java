package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.*;
import com.doctorcare.PD_project.dto.response.*;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.SocialNetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
public class SocialNetworkController {

    private final SocialNetworkService socialNetworkService;

    // **** POST **** //
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody CreatePostRequest request) throws AppException {
        PostResponse result = socialNetworkService.createPost(request);
        return ResponseEntity.ok(ApiResponse.<PostResponse>builder()
                .code(1000)
                .message("Post created successfully")
                .result(result)
                .build());
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@PathVariable String postId,
                                                                @RequestBody UpdatePostRequest request) throws AppException {
        PostResponse result = socialNetworkService.updatePost(postId, request);
        return ResponseEntity.ok(ApiResponse.<PostResponse>builder()
                .code(1000)
                .message("Post updated successfully")
                .result(result)
                .build());
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable String postId) throws AppException {
        PostResponse result = socialNetworkService.getPostById(postId);
        return ResponseEntity.ok(ApiResponse.<PostResponse>builder()
                .code(1000)
                .message("Post fetched successfully")
                .result(result)
                .build());
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPostsByUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<PostResponse> result = socialNetworkService.getAllPostsByUser(userId, page, size);
        return ResponseEntity.ok(ApiResponse.<List<PostResponse>>builder()
                .code(1000)
                .message("Posts fetched successfully")
                .result(result)
                .build());
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable String postId) throws AppException {
        socialNetworkService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{postId}/interact")
    public ResponseEntity<ApiResponse<Void>> interactPost(@PathVariable String postId) throws AppException {
        socialNetworkService.interactPost(postId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(1000)
                .message("Interaction processed")
                .build());
    }

    // **** DISCOVER & LATEST & FROM FOLLOWERS **** //
    @GetMapping("/posts/discover")
    public ResponseEntity<ApiResponse<List<PostResponse>>> discoverPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        Page<PostResponse> pageResult = socialNetworkService.discoverPosts(page, size);
        return ResponseEntity.ok(ApiResponse.<List<PostResponse>>builder()
                .code(1000)
                .message("Discover posts fetched successfully")
                .result(pageResult.getContent())
                .build());
    }

    @GetMapping("/posts/latest")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getLatestPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        Page<PostResponse> pageResult = socialNetworkService.getLatestPosts(page, size);
        return ResponseEntity.ok(ApiResponse.<List<PostResponse>>builder()
                .code(1000)
                .message("Latest posts fetched successfully")
                .result(pageResult.getContent())
                .build());
    }

    @GetMapping("/posts/from-followers")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getPostsFromFollowers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        Page<PostResponse> pageResult = socialNetworkService.getPostsFromFollowers(page, size);
        return ResponseEntity.ok(ApiResponse.<List<PostResponse>>builder()
                .code(1000)
                .message("Followers' posts fetched successfully")
                .result(pageResult.getContent())
                .build());
    }

    // **** COMMENT **** //
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> commentOnPost(@RequestBody CreateCommentRequest request) throws AppException {
        CommentResponse result = socialNetworkService.commentOnPost(request);
        return ResponseEntity.ok(ApiResponse.<CommentResponse>builder()
                .code(1000)
                .message("Comment added")
                .result(result)
                .build());
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(@PathVariable String commentId,
                                                                      @RequestBody UpdateCommentRequest request) throws AppException {
        CommentResponse result = socialNetworkService.updateComment(commentId, request);
        return ResponseEntity.ok(ApiResponse.<CommentResponse>builder()
                .code(1000)
                .message("Comment updated")
                .result(result)
                .build());
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable String commentId) throws AppException {
        socialNetworkService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getAllCommentsByPost(
            @PathVariable String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<CommentResponse> result = socialNetworkService.getAllCommentsByPost(postId, page, size);
        return ResponseEntity.ok(ApiResponse.<List<CommentResponse>>builder()
                .code(1000)
                .message("Comments fetched successfully")
                .result(result)
                .build());
    }

    // **** SAVE POST **** //
    @PostMapping("/posts/{postId}/save")
    public ResponseEntity<ApiResponse<Void>> toggleSave(@PathVariable String postId) throws AppException {
        socialNetworkService.toggleSave(postId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(1000)
                .message("Toggle save successfully")
                .build());
    }

    @GetMapping("/posts/saved")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllSavedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<PostResponse> result = socialNetworkService.getAllSavedPosts(page, size);
        return ResponseEntity.ok(ApiResponse.<List<PostResponse>>builder()
                .code(1000)
                .message("Saved posts fetched successfully")
                .result(result)
                .build());
    }

    // **** FOLLOW **** //
    @PostMapping("/users/follow")
    public ResponseEntity<ApiResponse<Void>> followUser(@RequestParam String targetUserId) throws AppException {
        socialNetworkService.followUser(targetUserId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(1000)
                .message("Follow/unfollow processed")
                .build());
    }

    // **** SUGGEST USERS TO FOLLOW **** //
    @GetMapping("/users/suggest-follow")
    public ResponseEntity<ApiResponse<List<BasicInfoUserResponse>>> suggestUsersToFollow(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        Page<BasicInfoUserResponse> pageResult = socialNetworkService.suggestUsersToFollow(page, size);
        return ResponseEntity.ok(ApiResponse.<List<BasicInfoUserResponse>>builder()
                .code(1000)
                .message("User suggestions fetched successfully")
                .result(pageResult.getContent())
                .build());
    }

    @GetMapping("/users/{userId}/followers")
    public ResponseEntity<ApiResponse<List<BasicInfoUserResponse>>> getAllFollowers(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<BasicInfoUserResponse> result = socialNetworkService.getAllFollowers(userId, page, size);
        return ResponseEntity.ok(ApiResponse.<List<BasicInfoUserResponse>>builder()
                .code(1000)
                .message("Followers fetched successfully")
                .result(result)
                .build());
    }

    @GetMapping("/users/{userId}/following")
    public ResponseEntity<ApiResponse<List<BasicInfoUserResponse>>> getAllFollowingUsers(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<BasicInfoUserResponse> result = socialNetworkService.getAllFollowingUsers(userId, page, size);
        return ResponseEntity.ok(ApiResponse.<List<BasicInfoUserResponse>>builder()
                .code(1000)
                .message("Following users fetched successfully")
                .result(result)
                .build());
    }

    @PostMapping("/users/accept-follow")
    public ResponseEntity<ApiResponse<Void>> acceptFollowRequest(@RequestParam String followRequestId) throws AppException {
        socialNetworkService.acceptFollowRequest(followRequestId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(1000)
                .message("Follow request accepted")
                .build());
    }

    @DeleteMapping("/users/reject-follow")
    public ResponseEntity<ApiResponse<Void>> rejectFollowRequest(@RequestParam String followRequestId) throws AppException {
        socialNetworkService.rejectFollowRequest(followRequestId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(1000)
                .message("Follow request rejected")
                .build());
    }

    @GetMapping("/users/follow-requests")
    public ResponseEntity<ApiResponse<List<BasicInfoUserResponse>>> getAllFollowRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<BasicInfoUserResponse> result = socialNetworkService.getAllFollowRequests(page, size);
        return ResponseEntity.ok(ApiResponse.<List<BasicInfoUserResponse>>builder()
                .code(1000)
                .message("Follow requests fetched successfully")
                .result(result)
                .build());
    }

    @PostMapping("/users/private")
    public ResponseEntity<ApiResponse<Void>> setPrivate() throws AppException {
        socialNetworkService.setPrivate();
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(1000)
                .message("Privacy setting toggled")
                .build());
    }
}
