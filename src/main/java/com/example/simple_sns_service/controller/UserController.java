package com.example.simple_sns_service.controller;

import com.example.simple_sns_service.controller.request.UserJoinRequest;
import com.example.simple_sns_service.controller.request.UserLoginRequest;
import com.example.simple_sns_service.controller.response.NotificationResponse;
import com.example.simple_sns_service.controller.response.Response;
import com.example.simple_sns_service.controller.response.UserJoinResponse;
import com.example.simple_sns_service.controller.response.UserLoginResponse;
import com.example.simple_sns_service.exception.ErrorCode;
import com.example.simple_sns_service.exception.SnsApplicationException;
import com.example.simple_sns_service.model.User;
import com.example.simple_sns_service.service.NotificationService;
import com.example.simple_sns_service.service.UserService;
import com.example.simple_sns_service.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;
    private final NotificationService notificationService;


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
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));
        return Response.success(userService.notificationList(user.getId(), pageable).map(NotificationResponse::fromNotification));
    }

    @GetMapping("/notification/subscribe")
    public SseEmitter subscribe(Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));
        return notificationService.connectNotification(user.getId());
    }
}
