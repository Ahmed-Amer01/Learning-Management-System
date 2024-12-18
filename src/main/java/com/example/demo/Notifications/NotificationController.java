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
    public List<Notification> getNotifications(@RequestBody NotificationRequest request) {
        return notificationService.getNotifications(request.getUserType(), request.getUserID());
    }

    @PatchMapping("/{notificationID}")
    public void patchNotification (@PathVariable Long notificationID) {
        notificationService.markNotificationAsRead(notificationID);
    }
}
