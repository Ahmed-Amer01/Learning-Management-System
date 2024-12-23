package com.example.lms.Notifications.NotificationsManager;

import com.example.lms.Notifications.Enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    @Query("""
           SELECT n 
           FROM Notification n 
           WHERE n.notificationData.receiverType = :receiverRole
           AND n.notificationData.receiverID = :receiverID
           AND (:isUnreadOnly = false OR n.isRead = false)
           """)
    List<Notification> retreiveNotificationsForUser(
            @Param("receiverRole") UserRole receiverRole,
            @Param("receiverID") String receiverID,
            @Param("isUnreadOnly") boolean isUnreadOnly
    );
}
