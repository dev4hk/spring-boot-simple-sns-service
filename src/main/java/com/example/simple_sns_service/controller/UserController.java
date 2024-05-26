package com.example.simple_sns_service.controller;

import com.example.simple_sns_service.controller.request.UserJoinRequest;
import com.example.simple_sns_service.controller.request.UserLoginRequest;
import com.example.simple_sns_service.controller.response.NotificationResponse;
import com.example.simple_sns_service.controller.response.Response;
import com.example.simple_sns_service.controller.response.UserJoinResponse;
import com.example.simple_sns_service.controller.response.UserLoginResponse;
import com.example.simple_sns_service.model.User;
import com.example.simple_sns_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/notification")
    public Response<Page<NotificationResponse>> notification(Pageable pageable, Authentication authentication) {
        return Response.success(userService.notificationList(authentication.getName(), pageable).map(NotificationResponse::fromNotification));
    }
}
