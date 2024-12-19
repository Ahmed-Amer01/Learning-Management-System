package com.example.demo.DTO;

import com.example.demo.Notifications.UserType;

public class NotificationRequest {
    private UserType userType;
    private String userID;
    private boolean unReadOnly;

    NotificationRequest(UserType userType, String userID, boolean unReadOnly) {
        this.userType = userType;
        this.userID = userID;
        this.unReadOnly = unReadOnly;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getUserID() {
        return userID;
    }

    public boolean isUnReadOnly() {
        return unReadOnly;
    }
}
