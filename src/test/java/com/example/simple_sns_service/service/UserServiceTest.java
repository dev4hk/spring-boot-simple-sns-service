package com.example.simple_sns_service.service;

import com.example.simple_sns_service.exception.ErrorCode;
import com.example.simple_sns_service.exception.SnsApplicationException;
import com.example.simple_sns_service.fixture.UserEntityFixture;
import com.example.simple_sns_service.model.entity.UserEntity;
import com.example.simple_sns_service.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void signup() {
        String userName = "username";
        String password = "password";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encryptedPassword");
        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(userName, password, 1));

        Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @Test
    void signup_with_existing_username() {
        String userName = "username";
        String password = "password";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(UserEntityFixture.get(userName, password, 1)));
        when(passwordEncoder.encode(password)).thenReturn("encryptedPassword");

        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(userName, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, exception.getErrorCode());
    }

    @Test
    void login() {
        String userName = "username";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(passwordEncoder.matches(password, fixture.getPassword())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.login(userName, password));
    }

    @Test
    void login_with_non_existing_credential() {
        String userName = "username";
        String password = "password";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(userName, password));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void login_with_wrong_password() {
        String userName = "username";
        String password = "password";
        String wrongPassword = "wrongPassword";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(UserEntityFixture.get(userName, wrongPassword, 1)));

        SnsApplicationException exception = Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(userName, password));
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }

}
