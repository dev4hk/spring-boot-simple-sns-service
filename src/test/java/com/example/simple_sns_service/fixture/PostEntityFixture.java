package com.example.simple_sns_service.fixture;

import com.example.simple_sns_service.model.entity.PostEntity;
import com.example.simple_sns_service.model.entity.UserEntity;

public class PostEntityFixture {
    public static PostEntity get(String userName, Integer postId, Integer userId) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUserName(userName);

        PostEntity result = new PostEntity();
        result.setUser(user);
        result.setId(postId);

        return result;
    }
}
