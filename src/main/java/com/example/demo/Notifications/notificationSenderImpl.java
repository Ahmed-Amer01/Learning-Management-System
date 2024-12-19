package com.example.demo.Notifications;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class notificationSenderImpl implements NotificationSender {

    NotificationService notificationService;

    notificationSenderImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void sendNewEnrollmentNotification(String receiverID,
                                       String enrolledStudentID,
                                       String courseName,
                                       LocalDateTime createdAt
    ) {
        String message = "A new student with ID: " + enrolledStudentID + " has enrolled in your course " + courseName + "\n";

        notificationService.addNotification(NotificationType.NEW_ENROLLMENT, UserType.INSTRUCTOR, receiverID, message, createdAt);
    }

    public void sendEnrollmentSuccessNotification(String receiverID,
                                           String courseName,
                                           LocalDateTime createdAt
    ) {
        String message = "You have successfully enrolled in " + courseName + "\n";

        notificationService.addNotification(NotificationType.ENROLLMENT_SUCCESS, UserType.STUDENT, receiverID, message, createdAt);
    }

    public void sendAssignmentGradedNotification(String receiverID,
                                          String assignmentName,
                                          String courseName,
                                          LocalDateTime createdAt
    ) {
        String message = "The grade of assignment " + assignmentName + " in " + courseName + " course is out now\n";

        notificationService.addNotification(NotificationType.ASSIGNMENT_GRADED, UserType.STUDENT, receiverID, message, createdAt);
    }

    public void sendQuizGradedNotification(String receiverID,
                                    String quizName,
                                    String courseName,
                                    LocalDateTime createdAt
    ) {
        String message = "The grade of quiz " + quizName + " in " + courseName + " course is out now\n";

        notificationService.addNotification(NotificationType.QUIZ_GRADED, UserType.STUDENT, receiverID, message, createdAt);
    }

    public void sendCourseUpdateNotification(List <String> receiversIDs,
                                             String courseName,
                                             LocalDateTime createdAt
    ) {
        String message = "There's a new item in " + courseName + " feed\n";
        for (String studentID : receiversIDs)
        {
            notificationService.addNotification(NotificationType.COURSE_UPDATE, UserType.STUDENT, studentID, message, createdAt);
        }
    }

}
