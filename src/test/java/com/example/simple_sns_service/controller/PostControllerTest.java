package com.example.simple_sns_service.controller;

import com.example.simple_sns_service.controller.request.PostCommentRequest;
import com.example.simple_sns_service.controller.request.PostCreateRequest;
import com.example.simple_sns_service.controller.request.PostModifyRequest;
import com.example.simple_sns_service.exception.ErrorCode;
import com.example.simple_sns_service.exception.SnsApplicationException;
import com.example.simple_sns_service.fixture.PostEntityFixture;
import com.example.simple_sns_service.model.Post;
import com.example.simple_sns_service.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @WithMockUser
    @Test
    void create_post() throws Exception {
        String title = "title";
        String body = "body";

        mockMvc.perform(
                        post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void create_post_without_login_returns_error() throws Exception {
        String title = "title";
        String body = "body";

        mockMvc.perform(
                        post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void update_post() throws Exception {
        String title = "title";
        String body = "body";

        when(postService.modify(eq(title), eq(body), any(), any()))
                .thenReturn(Post.fromEntity(PostEntityFixture.get("userName", 1, 1)));

        mockMvc.perform(
                        put("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void update_post_without_login_returns_error() throws Exception {
        String title = "title";
        String body = "body";

        mockMvc.perform(
                        put("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void update_post_by_another_user_returns_error() throws Exception {
        String title = "title";
        String body = "body";

        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body), any(), eq(1));

        mockMvc.perform(
                        put("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void update_non_existing_post_returns_error() throws Exception {
        String title = "title";
        String body = "body";

        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(title), eq(body), any(), eq(1));

        mockMvc.perform(
                        put("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    void delete_post() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void delete_post_without_login_returns_error() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void delete_post_by_another_user_returns_error() throws Exception {
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(), any());

        mockMvc.perform(
                        delete("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void delete_non_existing_post_returns_error() throws Exception {
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());

        mockMvc.perform(
                        delete("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @WithMockUser
    @Test
    void get_posts() throws Exception {
        when(postService.getAllPosts(any())).thenReturn(Page.empty());
        mockMvc.perform(
                        get("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void get_posts_without_login_returns_error() throws Exception {
        when(postService.getAllPosts(any())).thenReturn(Page.empty());
        mockMvc.perform(
                        get("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void get_my_posts() throws Exception {
        when(postService.getMyPosts(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(
                        get("/api/v1/posts/my")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void get_my_posts_without_login_returns_error() throws Exception {
        when(postService.getMyPosts(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(
                        get("/api/v1/posts/my")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void like_post() throws Exception {
        mockMvc.perform(
                        post("/api/v1/posts/1/likes")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void like_post_without_login_returns_error() throws Exception {
        mockMvc.perform(
                        post("/api/v1/posts/1/likes")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void like_non_existing_post_returns_error() throws Exception {
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).like(any(), any());
        mockMvc.perform(
                        post("/api/v1/posts/1/likes")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    void create_comment() throws Exception {
        mockMvc.perform(
                        post("/api/v1/posts/1/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @Test
    void create_comment_without_login_returns_error() throws Exception {
        mockMvc.perform(
                        post("/api/v1/posts/1/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void create_comment_on_non_existing_post_returns_error() throws Exception {
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).comment(any(), any(), any());
        mockMvc.perform(
                        post("/api/v1/posts/1/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostCommentRequest("comment")))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
