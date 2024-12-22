package com.example.demo.Notifications.NotificationsManager;

import com.example.demo.Notifications.Enums.UserRole;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public Notification addNotification(NotificationData notificationData) {
        String date_formatted = formatDate(notificationData.getCreatedAt());
        Notification notification = new Notification(notificationData, date_formatted, false);
        notificationRepository.create(notification);
        return notification;
    }

    public List <Notification> getNotifications(UserRole receiverType, String receiverID, boolean isUnreadOnly) {
        //didn's use the return of notificationRepository.retreiveNotificationsForUser directly, because it return an immutable list, which throws an exception when sorted. To make it mutable, we first need to explicitly put it in an ArrayList
        List<Notification> userNotifications = new ArrayList<>(notificationRepository.retreiveNotificationsForUser(receiverType, receiverID, isUnreadOnly));
        if (userNotifications.size() > 1)
        {
            userNotifications = userNotifications.stream()
                    .sorted((notification1, notification2) ->
                            notification2.getNotificationData().getCreatedAt()
                                    .compareTo(notification1.getNotificationData().getCreatedAt())
                    )
                    .collect(Collectors.toList());
        }
        return userNotifications;
    }

    public void markNotificationAsRead(String notificationID) {
        Notification target = notificationRepository.retrieveNotificationByID(notificationID);
        NotificationData newND = new NotificationData(target.notificationData().getNotificationType(), target.notificationData().getReceiverType(), target.notificationData().getReceiverID(), target.notificationData().getMessage(), target.notificationData().getCreatedAt());
        Notification newN = new Notification(target.notificationID(), newND, target.createdAt_formatted(), true);
        notificationRepository.delete(notificationID);
        notificationRepository.create(newN);
    }

}
