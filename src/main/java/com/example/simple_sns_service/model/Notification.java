package com.example.simple_sns_service.model;

import com.example.simple_sns_service.model.entity.NotificationEntity;
import com.example.simple_sns_service.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Notification {

    private Integer id;
//    private User user;
    private NotificationType notificationType;
    private NotificationArgument notificationArgument;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;


    public static Notification fromEntity(NotificationEntity entity) {
        return new Notification(
                entity.getId(),
//                User.fromEntity(entity.getUser()),
                entity.getNotificationType(),
                entity.getArgs(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}