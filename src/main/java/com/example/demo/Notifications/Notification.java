package com.example.demo.Notifications;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public record Notification (@Id
                            @GeneratedValue(strategy = GenerationType.IDENTITY)
                            Long notificationID,
                            NotificationType notificationType,
                            UserType receiverType,
                            List<String> receiversIDs,
                            String message,
                            LocalDateTime createdAt,
                            boolean isRead
) {
    //do some data validation logic
}
