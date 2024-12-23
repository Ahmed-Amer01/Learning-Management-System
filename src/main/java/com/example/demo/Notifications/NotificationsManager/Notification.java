package com.example.demo.Notifications.NotificationsManager;

import jakarta.persistence.*;
import lombok.*;

import java.util.concurrent.atomic.AtomicLong;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
        @Id
        @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
        private String notificationID;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "notification_data_id", referencedColumnName = "id")
        private NotificationData notificationData;

        @Column(nullable = false)
        String createdAt_formatted;

        @Column(nullable = false)
        boolean isRead;


        private static final AtomicLong counter = new AtomicLong();


        public Notification(NotificationData notificationData, String dateFormatted, boolean b) {
            this.notificationID = String.valueOf(counter.incrementAndGet());
            this.notificationData = notificationData;
            this.createdAt_formatted = dateFormatted;
            this.isRead = b;
        }

    //private static final AtomicLong counter = new AtomicLong();

    /*public Notification(
            NotificationData notificationData,
            String createdAt_formatted,
            boolean isRead
    ) {
        this(String.valueOf(counter.incrementAndGet()), notificationData, createdAt_formatted, isRead);
    }*/

    /*public String getNotificationID () {
        return notificationID;
    }

    public NotificationData getNotificationData() {
        return notificationData;
    }

    public String getCreatedAt_formatted () {
        return createdAt_formatted;
    }

    public boolean isRead () {
        return isRead;
    }*/

}
