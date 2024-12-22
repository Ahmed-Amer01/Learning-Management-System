package com.example.demo.Notifications.NotificationsManager;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.concurrent.atomic.AtomicLong;

@Entity
public record Notification (@Id
                            @GeneratedValue(strategy = GenerationType.IDENTITY)
                            String notificationID,

                            NotificationData notificationData,

                            String createdAt_formatted,
                            boolean isRead
) {
    private static final AtomicLong counter = new AtomicLong();

    public Notification(
            NotificationData notificationData,
            String createdAt_formatted,
            boolean isRead
    ) {
        this(String.valueOf(counter.incrementAndGet()), notificationData, createdAt_formatted, isRead);

    }

    public NotificationData getNotificationData() {
        return notificationData;
    }

}
