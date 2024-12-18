package com.example.demo.Notifications;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    NotificationRepository notificationRepository;

    NotificationService (NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    void addNotification(NotificationType notificationType,
                         UserType receiverType,
                         List<String> receiversIDs,
                         String message,
                         LocalDateTime createdAt
    ) {
        Notification notification = new Notification(null, notificationType, receiverType, receiversIDs, message, createdAt, false);
        notificationRepository.create(notification);
    }

    List <Notification> getNotifications(UserType receiverType, String receiverID) {
        List<Notification> userNotifications = notificationRepository.retreiveByUser(receiverType, receiverID);
        if (userNotifications.size() > 1)
        {
            userNotifications.sort((a, b) -> b.createdAt().compareTo(a.createdAt()));
        }
        return userNotifications;
    }

    void markNotificationAsRead(Long notificationID) {
        Notification target = notificationRepository.retrieveByID(notificationID);
        Notification newN = new Notification(target.notificationType(), target.receiverType(), target.receiversIDs(), target.message(), target.createdAt(), true);
        notificationRepository.delete(notificationID);
        notificationRepository.create(newN);
    }

}
