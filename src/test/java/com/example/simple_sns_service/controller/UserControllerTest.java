package com.example.simple_sns_service.controller;

import com.example.simple_sns_service.controller.request.UserJoinRequest;
import com.example.simple_sns_service.controller.request.UserLoginRequest;
import com.example.simple_sns_service.exception.ErrorCode;
import com.example.simple_sns_service.exception.SnsApplicationException;
import com.example.simple_sns_service.model.User;
import com.example.simple_sns_service.service.UserService;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void signup() throws Exception {
        String userName = "username";
        String password = "password";

        when(userService.join(userName, password)).thenReturn(mock(User.class));

        mockMvc.perform(
                        post("/api/v1/users/join")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void signup_duplicate_account_returns_error() throws Exception {
        String userName = "username";
        String password = "password";

        when(userService.join(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mockMvc.perform(
                        post("/api/v1/users/join")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
                )
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void login() throws Exception {
        String userName = "username";
        String password = "password";

        when(userService.login(userName, password)).thenReturn("test_token");

        mockMvc.perform(
                        post("/api/v1/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void login_with_non_existing_credential_returns_error() throws Exception {
        String userName = "username";
        String password = "password";

        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(
                        post("/api/v1/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void login_with_wrong_password_returns_error() throws Exception {
        String userName = "username";
        String password = "password";

        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(
                        post("/api/v1/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    void notification() throws Exception {
        when(userService.notificationList(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(get("/api/v1/users/notification")
                        .contentType(MediaType.APPLICATION_JSON
                        ))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @WithAnonymousUser
    @Test
    void notification_without_login() throws Exception {
        when(userService.notificationList(any(), any())).thenReturn(Page.empty());
        mockMvc.perform(get("/api/v1/users/notification")
                        .contentType(MediaType.APPLICATION_JSON
                        ))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }
}
