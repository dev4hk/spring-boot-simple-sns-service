package com.example.simple_sns_service.repository;

import com.example.simple_sns_service.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
     Optional<UserEntity> findByUserName(String userName);
}
