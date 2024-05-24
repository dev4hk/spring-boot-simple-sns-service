package com.example.simple_sns_service.controller;

import com.example.simple_sns_service.controller.request.UserJoinRequest;
import com.example.simple_sns_service.controller.request.UserLoginRequest;
import com.example.simple_sns_service.exception.SnsApplicationException;
import com.example.simple_sns_service.model.User;
import com.example.simple_sns_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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

        when(userService.join()).thenReturn(mock(User.class));

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

        when(userService.join()).thenThrow(new SnsApplicationException());

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

        when(userService.login()).thenReturn("test_token");

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

        when(userService.login()).thenThrow(new SnsApplicationException());

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

        when(userService.login()).thenThrow(new SnsApplicationException());

        mockMvc.perform(
                        post("/api/v1/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
