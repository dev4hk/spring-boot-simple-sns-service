package com.example.simple_sns_service.controller;

import com.example.simple_sns_service.controller.request.UserJoinRequest;
import com.example.simple_sns_service.controller.response.Response;
import com.example.simple_sns_service.controller.response.UserJoinResponse;
import com.example.simple_sns_service.model.User;
import com.example.simple_sns_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getUserName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }
}
