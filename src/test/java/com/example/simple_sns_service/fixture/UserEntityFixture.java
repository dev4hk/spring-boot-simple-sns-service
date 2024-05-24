package com.example.simple_sns_service.fixture;

import com.example.simple_sns_service.model.entity.UserEntity;

public class UserEntityFixture {
    public static UserEntity get(String userName, String password) {
        UserEntity result = new UserEntity();
        result.setId(1);
        result.setUserName(userName);
        result.setPassword(password);

        return result;
    }
}
