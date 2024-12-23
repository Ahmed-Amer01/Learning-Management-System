package com.example.lms.Notifications.NotificationCreator;

import com.example.lms.Notifications.NotificationsManager.NotificationData;
import com.example.lms.Notifications.Enums.NotificationType;
import com.example.lms.Notifications.Enums.UserRole;

public class QuizGradedCreator extends NotificationCreator {

    private String quizName;

    public QuizGradedCreator(String receiverID, String courseName, String quizName) {
        super(receiverID, courseName);
        this.quizName = quizName;
    }

    public NotificationData createQuizGradedNotification() {
        String message = "The grade of " + quizName + " in " + courseName + " course is out now\n";
        return new NotificationData(NotificationType.QUIZ_GRADED, UserRole.STUDENT, receiverID, message, createdAt);
    }
}
