package com.example.demo.Notifications;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

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
    private static final AtomicLong counter = new AtomicLong();

    public Notification(
            NotificationType notificationType,
            UserType receiverType,
            List<String> receiversIDs,
            String message,
            LocalDateTime createdAt,
            boolean isRead
    ) {
        this(counter.incrementAndGet(), notificationType, receiverType, receiversIDs, message, createdAt, isRead);
    }
}
