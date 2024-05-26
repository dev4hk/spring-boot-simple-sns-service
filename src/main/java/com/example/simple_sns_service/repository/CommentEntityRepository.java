package com.example.simple_sns_service.repository;

import com.example.simple_sns_service.model.entity.CommentEntity;
import com.example.simple_sns_service.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
    Page<CommentEntity> findAllByPost(PostEntity postEntity, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE CommentEntity entity SET deleted_at = NOW() where entity.post = :post")
    void deleteAllByPost(@Param("post")PostEntity postEntity);
}
