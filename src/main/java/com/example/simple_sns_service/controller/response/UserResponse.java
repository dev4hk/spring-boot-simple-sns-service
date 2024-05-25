package com.example.simple_sns_service.controller.response;

import com.example.simple_sns_service.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String userName;

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername()
        );
    }

}
