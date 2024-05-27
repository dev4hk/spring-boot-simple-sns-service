package com.example.simple_sns_service.service;

import com.example.simple_sns_service.exception.ErrorCode;
import com.example.simple_sns_service.exception.SnsApplicationException;
import com.example.simple_sns_service.model.Notification;
import com.example.simple_sns_service.model.User;
import com.example.simple_sns_service.model.entity.UserEntity;
import com.example.simple_sns_service.repository.NotificationEntityRepository;
import com.example.simple_sns_service.repository.UserCacheRepository;
import com.example.simple_sns_service.repository.UserEntityRepository;
import com.example.simple_sns_service.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final NotificationEntityRepository notificationEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCacheRepository userCacheRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    @Transactional
    public User join(String userName, String password) {
        userEntityRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));
                });
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, passwordEncoder.encode(password)));
        return User.fromEntity(userEntity);
    }

    public String login(String userName, String password) {
        User user = loadUserByUserName(userName);
        userCacheRepository.setUser(user);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        return JwtTokenUtils.generateToken(userName, this.secretKey, this.expiredTimeMs);
    }

    public User loadUserByUserName(String userName) {
        return userCacheRepository.getUser(userName).orElseGet(
                () -> userEntityRepository.findByUserName(userName).map(User::fromEntity)
                .orElseThrow(
                        () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName))));
    }

    public Page<Notification> notificationList(Integer userId, Pageable pageable) {
        return notificationEntityRepository.findAllByUserId(userId, pageable).map(Notification::fromEntity);
    }
}
