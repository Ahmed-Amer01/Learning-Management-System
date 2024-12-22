package com.example.demo.Notifications.NotificationCreator;

import com.example.demo.Notifications.NotificationsManager.NotificationData;
import com.example.demo.Notifications.Enums.NotificationType;
import com.example.demo.Notifications.Enums.UserRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentCreator extends NotificationCreator {
    private String enrolledStudentID;
    private String courseInstructorID;

    public EnrollmentCreator(String receiverID, String courseName, String enrolledStudentID, String courseInstructorID) {
        super(receiverID, courseName);
        this.enrolledStudentID = enrolledStudentID;
        this.courseInstructorID = courseInstructorID;
    }

    public List<NotificationData> createNewEnrollmentNotification() {
        String messageToInstructor = "A new student with ID: " + enrolledStudentID + " has enrolled in your course " + courseName + "\n";
        NotificationData notificationData1 = new NotificationData(NotificationType.NEW_ENROLLMENT, UserRole.INSTRUCTOR, courseInstructorID, messageToInstructor, createdAt);

        String messageToStudent = "You have successfully enrolled in " + courseName + " course\n";
        NotificationData notificationData2 = new NotificationData(NotificationType.ENROLLMENT_SUCCESS, UserRole.STUDENT, enrolledStudentID, messageToStudent, createdAt);

        List<NotificationData> notificationDataList = new ArrayList<NotificationData>();
        notificationDataList.add(notificationData1);
        notificationDataList.add(notificationData2);

        return notificationDataList;
    }
}
