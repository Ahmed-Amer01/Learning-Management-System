package com.example.lms.Notifications.NotificationsManager;

import com.example.lms.Notifications.DTOs.NotificationRequest;
import com.example.lms.Notifications.NotificationCreator.AssignmentGradedCreator;
import com.example.lms.Notifications.NotificationCreator.CourseUpdateCreator;
import com.example.lms.Notifications.NotificationCreator.EnrollmentCreator;
import com.example.lms.Notifications.NotificationCreator.QuizGradedCreator;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class NotificationController {

    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }



    @PostMapping("{courseID}/success")
    public List<Notification> PostEnrollmentNotification(@RequestBody EnrollmentCreator enrollmentCreator, @PathVariable int courseID) throws IOException, InterruptedException {
        List<Notification> ret = new ArrayList<>();
        ret.add(notificationService.addNotification(enrollmentCreator.createNewEnrollmentNotification().get(0)));
        ret.add(notificationService.addNotification(enrollmentCreator.createNewEnrollmentNotification().get(1)));
        for (Notification notification : ret) {
            notificationService.sendNotificationByEmail(notification);
        }
        return ret;
    }

    @ResponseBody
    @PostMapping("{courseID}/{assignmentID}/mark")
    public Notification PostAssignmentGradedNotification(@RequestBody AssignmentGradedCreator assignmentGradedCreator, @PathVariable int courseID, @PathVariable int assignmentID) throws IOException, InterruptedException {
        Notification generatedNotification = notificationService.addNotification(assignmentGradedCreator.createAssignmentGradedNotification());
        notificationService.sendNotificationByEmail(generatedNotification);
        return generatedNotification;
    }

    @ResponseBody
    @PostMapping("{courseID}/{quizID}/grade")
    public Notification PostQuizGradedNotification(@RequestBody QuizGradedCreator quizGradedCreator, @PathVariable int courseID, @PathVariable int quizID) throws IOException, InterruptedException {
        Notification generatedNotification = notificationService.addNotification(quizGradedCreator.createQuizGradedNotification());
        notificationService.sendNotificationByEmail(generatedNotification);
        return generatedNotification;
    }

    @ResponseBody
    @PostMapping("{courseID}/upload")
    public Notification PostCourseUpdateNotification(@RequestBody CourseUpdateCreator courseUpdateCreator, @PathVariable int courseID) throws IOException, InterruptedException {
        Notification generatedNotification = notificationService.addNotification(courseUpdateCreator.createCourseUpdateNotification());
        notificationService.sendNotificationByEmail(generatedNotification);
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
