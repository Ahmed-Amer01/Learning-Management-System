package com.example.demo.Notifications.NotificationCreator;

import com.example.demo.Notifications.NotificationsManager.NotificationData;
import com.example.demo.Notifications.Enums.NotificationType;
import com.example.demo.Notifications.Enums.UserRole;

import java.time.LocalDateTime;

public class AssignmentGradedCreator extends NotificationCreator {

    private String assignmentName;

    public AssignmentGradedCreator(String receiverID, String courseName, String assignmentName) {
        super(receiverID, courseName);
        this.assignmentName = assignmentName;
    }

    public NotificationData createAssignmentGradedNotification() {
        String message = "The grade of assignment " + assignmentName + " in " + courseName + " course is out now\n";
        return new NotificationData(NotificationType.ASSIGNMENT_GRADED, UserRole.STUDENT, receiverID, message, createdAt);
    }
}
