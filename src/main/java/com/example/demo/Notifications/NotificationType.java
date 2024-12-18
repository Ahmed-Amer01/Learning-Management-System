package com.example.demo.Notifications;

public enum NotificationType {
    //The only type for instructors
    NEW_ENROLLMENT,
    //The others are for students
    ENROLLMENT_SUCCESS,
    ASSIGNMENT_GRADED,
    QUIZ_GRADED,
    //the only type that has an array of receivers
    COURSE_UPDATE
}
