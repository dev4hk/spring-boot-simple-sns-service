package com.example.simple_sns_service.repository;

import com.example.simple_sns_service.model.entity.NotificationEntity;
import com.example.simple_sns_service.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, Integer> {
    Page<NotificationEntity> findAllByUserId(Integer userId, Pageable pageable);
}
