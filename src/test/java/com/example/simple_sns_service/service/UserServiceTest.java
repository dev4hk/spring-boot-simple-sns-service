package com.example.simple_sns_service.service;

import com.example.simple_sns_service.exception.SnsApplicationException;
import com.example.simple_sns_service.model.entity.UserEntity;
import com.example.simple_sns_service.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    @Test
    void signup() {
        String userName = "username";
        String password = "password";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(userEntityRepository.save(any())).thenReturn(Optional.of(mock(UserEntity.class)));

        Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @Test
    void signup_with_existing_username() {
        String userName = "username";
        String password = "password";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));

        Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(userName, password));
    }

}
