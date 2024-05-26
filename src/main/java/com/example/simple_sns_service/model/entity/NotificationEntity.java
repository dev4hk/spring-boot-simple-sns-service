package com.example.simple_sns_service.model.entity;

import com.example.simple_sns_service.model.NotificationArgument;
import com.example.simple_sns_service.model.NotificationType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@TypeDef(name = "json", typeClass = JsonStringType.class)
@Data
@Entity
@Table(name = "\"notification\"", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@SQLDelete(sql = "UPDATE \"notification\" SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private NotificationArgument args;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static NotificationEntity of(UserEntity userEntity, NotificationType notificationType, NotificationArgument notificationArgument) {
        NotificationEntity entity = new NotificationEntity();
        entity.setUser(userEntity);
        entity.setNotificationType(notificationType);
        entity.setArgs(notificationArgument);
        return entity;
    }

}
