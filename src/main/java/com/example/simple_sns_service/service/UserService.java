package com.example.simple_sns_service.service;

import com.example.simple_sns_service.exception.ErrorCode;
import com.example.simple_sns_service.exception.SnsApplicationException;
import com.example.simple_sns_service.model.User;
import com.example.simple_sns_service.model.entity.UserEntity;
import com.example.simple_sns_service.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User join(String userName, String password) {
        userEntityRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));
                });
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, passwordEncoder.encode(password)));
        return User.fromEntity(userEntity);
    }

    // TODO: implement
    public String login(String userName, String password) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        if (!userEntity.getPassword().equals(password)) {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, "");
        }
        return "";
    }
}
