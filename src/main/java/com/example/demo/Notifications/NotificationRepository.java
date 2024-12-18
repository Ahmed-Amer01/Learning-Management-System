package com.example.demo.Notifications;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NotificationRepository {

    private List<Notification> notifications = new ArrayList<>();

    public List<Notification> getNotificationsOfUser(UserType type, String ID) {
        return notifications.stream().filter(notification -> notification.receiversIDs().contains(ID) && notification.receiverType() == type).toList();
    }

    void create (Notification notification) {
        notifications.add(notification);
    }

    List<Notification> retreiveByUser(UserType type, String ID) {
        return notifications.stream().filter(notification -> notification.receiverType() == type && notification.receiversIDs().contains(ID)).toList();
    }

    Notification retrieveByID (Long notificationID) {
        return notifications.stream()
                .filter(notification -> notification.notificationID().equals(notificationID)) // Filters notifications
                .findFirst() // Retrieves the first matching notification as an Optional
                .orElse(null); // Returns null if no match is found
    }

    void delete (Long notificationID) {
        notifications.removeIf(notification -> notification.notificationID().equals(notificationID));
    }

    @PostConstruct
    private void init() {
        List <String> studentID = new ArrayList<>();
        studentID.add("1");
        String message = "assignment one in OS has been graded";
        LocalDateTime date = LocalDateTime.now();
        notifications.add(new Notification(
                NotificationType.ASSIGNMENT_GRADED,
                UserType.STUDENT,
                studentID,
                message,
                date,
                false
        ));
    }
}
