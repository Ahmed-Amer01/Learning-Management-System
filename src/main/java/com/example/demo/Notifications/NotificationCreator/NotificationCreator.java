package com.example.demo.Notifications.NotificationCreator;

import java.time.LocalDateTime;

class NotificationCreator {

    public String receiverID;
    public String courseName;
    public LocalDateTime createdAt;

    public NotificationCreator(String receiverID, String courseName) {
        this.receiverID = receiverID;
        this.courseName = courseName;
        this.createdAt = LocalDateTime.now();
    }

}
