package com.example.demo.Notifications;

import com.example.demo.DTO.NotificationRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("notifications")
@RestController
public class NotificationController {

    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("")
    public List<Notification> getNotifications(@RequestBody NotificationRequest request, @PathVariable String studentID) {
        return notificationService.getNotifications(request.getUserType(), studentID);
    }

    @PatchMapping("/{notificationID}")
    public void patchNotification (@PathVariable Long notificationID, @PathVariable String studentID) {
        notificationService.markNotificationAsRead(notificationID);
    }
}
