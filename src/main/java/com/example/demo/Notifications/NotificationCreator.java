package com.example.demo.Notifications;

import java.time.LocalDateTime;
import java.util.List;

interface NotificationCreator {

    void sendNewEnrollmentNotification(String receiverID,
                                       String enrolledStudentID,
                                       String courseName,
                                       LocalDateTime createdAt
    );

    void sendEnrollmentSuccessNotification(String receiverID,
                                           String courseName,
                                           LocalDateTime createdAt
    );

    void sendAssignmentGradedNotification(String receiverID,
                                          String assignmentName,
                                          String courseName,
                                          LocalDateTime createdAt
    );

    void sendQuizGradedNotification(String receiverID,
                                    String quizName,
                                    String courseName,
                                    LocalDateTime createdAt
    );

    void sendCourseUpdateNotification( List<String> receiversIDs,
                                      String courseName,
                                      LocalDateTime createdAt
    );

}
