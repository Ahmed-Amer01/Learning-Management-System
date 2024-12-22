package com.example.demo.Notifications.NotificationsManager;



import com.example.demo.Notifications.Enums.NotificationType;
import com.example.demo.Notifications.Enums.UserRole;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NotificationRepository {

    private List<Notification> notifications = new ArrayList<>();

    void create (Notification notification) {
        notifications.add(notification);
    }

    List<Notification> retreiveNotificationsForUser(UserRole userRole, String userID, boolean isUnreadOnly) {
        return notifications.stream()
                .filter(notification ->
                        notification.notificationData().getReceiverType() == userRole &&
                        notification.notificationData().getReceiverID().equals(userID) &&
                        (isUnreadOnly ? !notification.isRead() : true)
                )
                .toList();
    }

    Notification retrieveNotificationByID(Long notificationID) {
        return notifications.stream()
                .filter(notification -> notification.notificationID().equals(notificationID))
                .findFirst()
                .orElse(null);
    }

    void delete (Long notificationID) {
        notifications.removeIf(notification -> notification.notificationID().equals(notificationID));
    }

    @PostConstruct
    private void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d/M/yyyy hh:mm a");


        String userID1 = "1";
        String message1 = "assignment one in OS has been graded";
        LocalDateTime date1 = LocalDateTime.now();
        String date1_formatted = date1.format(formatter);

        notifications.add(new Notification(
                new NotificationData(
                        NotificationType.ASSIGNMENT_GRADED,
                        UserRole.STUDENT,
                        userID1,
                        message1,
                        date1
                ),
                date1_formatted,
                false
        ));


        String userID2 = "1";
        String message2 = "There's a new update in algorithm's course stream";
        LocalDateTime date2 = LocalDateTime.now().plusHours(1);;
        String date2_formatted = date2.format(formatter);

        notifications.add(new Notification(
                new NotificationData(NotificationType.COURSE_UPDATE,
                                    UserRole.STUDENT,
                                    userID2,
                                    message2,
                                    date2
                ),
                date2_formatted,
                false
        ));


        String userID3 = "2";
        String message3 = "There's a new update in algorithm's course stream";
        LocalDateTime date3 = LocalDateTime.now().plusHours(2);
        String date3_formatted = date3.format(formatter);

        notifications.add(new Notification(
                new NotificationData(NotificationType.COURSE_UPDATE,
                        UserRole.STUDENT,
                        userID3,
                        message3,
                        date3
                ),
                date3_formatted,
                false
        ));

        String userID4 = "1";
        String message4 = "You have successfully enrolled in Advanced Software Engineering course!";
        LocalDateTime date4 = LocalDateTime.now().plusHours(3);;
        String date4_formatted = date4.format(formatter);

        notifications.add(new Notification(
                new NotificationData(NotificationType.ENROLLMENT_SUCCESS,
                        UserRole.STUDENT,
                        userID4,
                        message4,
                        date4
                ),
                date4_formatted,
                false
        ));
    }
}
