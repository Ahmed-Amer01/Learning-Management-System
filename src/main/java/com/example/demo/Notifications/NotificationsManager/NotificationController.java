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
        return ret;
    }

    @ResponseBody
    @PostMapping("{courseID}/{assignmentID}/mark")
    public Notification PostAssignmentGradedNotification(@RequestBody AssignmentGradedCreator assignmentGradedCreator, @PathVariable int courseID, @PathVariable int assignmentID) {
        return notificationService.addNotification(assignmentGradedCreator.createAssignmentGradedNotification());
    }

    @ResponseBody
    @PostMapping("{courseID}/{quizID}/grade")
    public Notification PostQuizGradedNotification(@RequestBody QuizGradedCreator quizGradedCreator, @PathVariable int courseID, @PathVariable int quizID) {
        return notificationService.addNotification(quizGradedCreator.createQuizGradedNotification());
    }

    @ResponseBody
    @PostMapping("{courseID}/upload")
    public Notification PostCourseUpdateNotification(@RequestBody CourseUpdateCreator courseUpdateCreator, @PathVariable int courseID) {
        return notificationService.addNotification(courseUpdateCreator.createCourseUpdateNotification());
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
