package com.example.simple_sns_service.controller.response;

import com.example.simple_sns_service.model.User;
import com.example.simple_sns_service.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    private String token;
}