package com.example.demo.Notifications;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class NotificationService {

    NotificationRepository notificationRepository;

    NotificationService (NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    private String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d/M/yyyy hh:mm a");
        return date.format(formatter);
    }

    void addNotification(NotificationType notificationType,
                         UserRole receiverType,
                        String receiverID,
                         String message,
                         LocalDateTime createdAt
    ) {
        String date_formatted = formatDate(createdAt);
        Notification notification = new Notification(null, notificationType, receiverType, receiverID, message, createdAt, date_formatted, false);
        notificationRepository.create(notification);
    }

    List <Notification> getNotifications(UserRole receiverType, String receiverID, boolean isUnreadOnly) {
        //didn's use the return of notificationRepository.retreiveNotificationsForUser directly, because it return an immutable list, which throws an exception when sorted. To make it mutable, we first need to explicitly put it in an ArrayList
        List<Notification> userNotifications = new ArrayList<>(notificationRepository.retreiveNotificationsForUser(receiverType, receiverID, isUnreadOnly));
        if (userNotifications.size() > 1)
        {
            userNotifications.sort(Comparator.comparing(Notification::getCreatedAt).reversed());
        }
        return userNotifications;
    }

    void markNotificationAsRead(Long notificationID) {
        Notification target = notificationRepository.retrieveNotificationByID(notificationID);
        Notification newN = new Notification(target.notificationID(), target.notificationType(), target.receiverType(), target.receiverID(), target.message(), target.createdAt(), target.createdAt_formatted(), true);
        notificationRepository.delete(notificationID);
        notificationRepository.create(newN);
    }

}
