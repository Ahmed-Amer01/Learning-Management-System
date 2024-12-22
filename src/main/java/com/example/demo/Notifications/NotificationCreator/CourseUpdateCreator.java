package com.example.demo.Notifications.NotificationCreator;

import com.example.demo.Notifications.NotificationsManager.NotificationData;
import com.example.demo.Notifications.Enums.NotificationType;
import com.example.demo.Notifications.Enums.UserRole;

import java.time.LocalDateTime;

public class CourseUpdateCreator extends NotificationCreator {

    public CourseUpdateCreator(String receiverID, String courseName) {
        super(receiverID, courseName);
    }

    public NotificationData createCourseUpdateNotification() {
        String message = "There's a new item in " + courseName + " feed\n";
        return new NotificationData(NotificationType.COURSE_UPDATE, UserRole.STUDENT, receiverID, message, createdAt);
    }
}
