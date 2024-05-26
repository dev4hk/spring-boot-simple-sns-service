package com.example.simple_sns_service.controller;

import com.example.simple_sns_service.controller.request.PostCreateRequest;
import com.example.simple_sns_service.controller.request.PostModifyRequest;
import com.example.simple_sns_service.controller.response.PostResponse;
import com.example.simple_sns_service.controller.response.Response;
import com.example.simple_sns_service.model.Post;
import com.example.simple_sns_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getTitle(), request.getBody(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication) {
        Post post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);
        return Response.success(PostResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);
        return Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse>> getAllPosts(Pageable pageable, Authentication authentication) {
        return Response.success(postService.getAllPosts(pageable).map(PostResponse::fromPost));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> getMyPosts(Pageable pageable, Authentication authentication) {
        return Response.success(postService.getMyPosts(pageable, authentication.getName()).map(PostResponse::fromPost));
    }

    @PostMapping("/{postId}/like")
    public Response<Void> like(@PathVariable Integer postId, Authentication authentication) {
        postService.like(postId, authentication.getName());
        return Response.success();
    }

}
