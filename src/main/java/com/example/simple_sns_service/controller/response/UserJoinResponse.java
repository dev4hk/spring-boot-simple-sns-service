package com.example.simple_sns_service.controller.response;

import com.example.simple_sns_service.model.User;
import com.example.simple_sns_service.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    private Integer id;
    private String userName;
    private UserRole role;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUserName(),
                user.getUserRole()
        );
    }
}
