package com.example.simple_sns_service.repository;

import com.example.simple_sns_service.model.entity.PostEntity;
import com.example.simple_sns_service.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {
    Page<PostEntity> findAllByUser(UserEntity entity, Pageable pageable);
}
