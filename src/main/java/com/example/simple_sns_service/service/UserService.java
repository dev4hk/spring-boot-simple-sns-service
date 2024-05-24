package com.example.simple_sns_service.service;

import com.example.simple_sns_service.model.User;
import com.example.simple_sns_service.model.entity.UserEntity;
import com.example.simple_sns_service.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;

    // TODO: implement
    public User join(String userName, String password) {
        Optional<UserEntity> userEntity = userEntityRepository.findByUserName(userName);
        userEntityRepository.save(new UserEntity());
        return new User();
    }

    // TODO: implement
    public String login() {
        return "";
    }
}
