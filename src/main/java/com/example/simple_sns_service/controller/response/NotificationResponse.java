package com.example.simple_sns_service.controller.response;

import com.example.simple_sns_service.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class NotificationResponse {

    private Integer id;
    private NotificationType notificationType;
    private NotificationArgument notificationArgument;
    private String text;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static NotificationResponse fromNotification(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getNotificationType(),
                notification.getNotificationArgument(),
                notification.getNotificationType().getNotificationText(),
                notification.getRegisteredAt(),
                notification.getUpdatedAt(),
                notification.getDeletedAt()
        );
    }
}
