package com.example.demo.Notifications;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Entity
public record Notification (@Id
                            @GeneratedValue(strategy = GenerationType.IDENTITY)
                            Long notificationID,

                            NotificationType notificationType,
                            UserType receiverType,
                            String receiverID,
                            String message,
                            LocalDateTime createdAt,
                            String createdAt_formatted,
                            boolean isRead
) {
    private static final AtomicLong counter = new AtomicLong();

    public Notification(
            NotificationType notificationType,
            UserType receiverType,
            String receiverID,
            String message,
            LocalDateTime createdAt,
            String createdAt_formatted,
            boolean isRead
    ) {
        this(counter.incrementAndGet(), notificationType, receiverType, receiverID, message, createdAt, createdAt_formatted, isRead);

    }

    public LocalDateTime getCreatedAt () {
        return createdAt;
    }
}
