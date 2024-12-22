package com.example.demo.Notifications.NotificationCreator;

import com.example.demo.Notifications.NotificationsManager.NotificationData;
import com.example.demo.Notifications.Enums.NotificationType;
import com.example.demo.Notifications.Enums.UserRole;

import java.time.LocalDateTime;

public class QuizGradedCreator extends NotificationCreator {

    private String quizName;

    public QuizGradedCreator(String receiverID, String courseName, String quizName) {
        super(receiverID, courseName);
        this.quizName = quizName;
    }

    public NotificationData createQuizGradedNotification() {
        String message = "The grade of assignment " + quizName + " in " + courseName + " course is out now\n";
        return new NotificationData(NotificationType.QUIZ_GRADED, UserRole.STUDENT, receiverID, message, createdAt);
    }
}
