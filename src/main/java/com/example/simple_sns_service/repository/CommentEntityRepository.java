package com.example.simple_sns_service.repository;

import com.example.simple_sns_service.model.entity.CommentEntity;
import com.example.simple_sns_service.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
    Page<CommentEntity> findAllByPost(PostEntity postEntity, Pageable pageable);
}
