package com.example.lms.Notifications.DTOs;

import com.example.lms.Notifications.Enums.UserRole;

public class NotificationRequest {
    private UserRole userRole;
    private String userID;
    private boolean unReadOnly;

    NotificationRequest(UserRole userRole, String userID, boolean unReadOnly) {
        this.userRole = userRole;
        this.userID = userID;
        this.unReadOnly = unReadOnly;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public String getUserID() {
        return userID;
    }

    public boolean isUnReadOnly() {
        return unReadOnly;
    }
}
