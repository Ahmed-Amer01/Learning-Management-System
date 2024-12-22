package com.example.demo.Notifications.NotificationsManager;

import com.example.demo.Notifications.Enums.NotificationType;
import com.example.demo.Notifications.Enums.UserRole;

import java.time.LocalDateTime;

public record NotificationData (NotificationType notificationType,
                                UserRole receiverType,
                                String receiverID,
                                String message,
                                LocalDateTime createdAt
) {

    public NotificationData(NotificationType notificationType, UserRole receiverType, String receiverID, String message, LocalDateTime createdAt) {
        this.notificationType = notificationType;
        this.receiverType = receiverType;
        this.receiverID = receiverID;
        this.message = message;
        this.createdAt = createdAt;
    }

    NotificationType getNotificationType() {
        return notificationType;
    }

    UserRole getReceiverType() {
        return receiverType;
    }

    String getReceiverID() {
        return receiverID;
    }

    String getMessage() {
        return message;
    }

    LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
