package com.example.demo.Notifications.NotificationsManager;

import com.example.demo.Notifications.DTOs.NotificationRequest;
import com.example.demo.Notifications.NotificationCreator.AssignmentGradedCreator;
import com.example.demo.Notifications.NotificationCreator.CourseUpdateCreator;
import com.example.demo.Notifications.NotificationCreator.EnrollmentCreator;
import com.example.demo.Notifications.NotificationCreator.QuizGradedCreator;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class NotificationController {

    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }




    @PostMapping("{courseID}/success")
    public List<Notification> PostEnrollmentNotification(@RequestBody EnrollmentCreator enrollmentCreator, @PathVariable int courseID) {
        List<Notification> ret = new ArrayList<>();
        ret.add(notificationService.addNotification(enrollmentCreator.createNewEnrollmentNotification().get(0)));
        ret.add(notificationService.addNotification(enrollmentCreator.createNewEnrollmentNotification().get(1)));
        for (Notification notification : ret) {
            notificationService.sendNotificationByEmail("ahalfy2005@gmail.com", notification);
        }
        return ret;
    }

    @ResponseBody
    @PostMapping("{courseID}/{assignmentID}/mark")
    public Notification PostAssignmentGradedNotification(@RequestBody AssignmentGradedCreator assignmentGradedCreator, @PathVariable int courseID, @PathVariable int assignmentID) {
        Notification generatedNotification = notificationService.addNotification(assignmentGradedCreator.createAssignmentGradedNotification());
        notificationService.sendNotificationByEmail("ahalfy2005h@gmail.com", generatedNotification);
        return generatedNotification;
    }

    @ResponseBody
    @PostMapping("{courseID}/{quizID}/grade")
    public Notification PostQuizGradedNotification(@RequestBody QuizGradedCreator quizGradedCreator, @PathVariable int courseID, @PathVariable int quizID) {
        Notification generatedNotification = notificationService.addNotification(quizGradedCreator.createQuizGradedNotification());
        notificationService.sendNotificationByEmail("ahalfy2005h@gmail.com", generatedNotification);
        return generatedNotification;
    }

    @ResponseBody
    @PostMapping("{courseID}/upload")
    public Notification PostCourseUpdateNotification(@RequestBody CourseUpdateCreator courseUpdateCreator, @PathVariable int courseID) {
        Notification generatedNotification = notificationService.addNotification(courseUpdateCreator.createCourseUpdateNotification());
        notificationService.sendNotificationByEmail("ahalfy2005h@gmail.com", generatedNotification);
        return generatedNotification;
    }




    @GetMapping("notifications")
    @ResponseBody
    public List<Notification> getNotifications(@RequestBody NotificationRequest request) {
        return notificationService.getNotifications(request.getUserRole(), request.getUserID(), request.isUnReadOnly());
    }




    @PatchMapping("notifications/{notificationID}")
    public void patchNotification (@PathVariable String notificationID) {
        notificationService.markNotificationAsRead(notificationID);
    }
}
