package com.example.demo.DTO;

import com.example.demo.Notifications.UserType;

public class NotificationRequest {
    private UserType userType;
    private String userID;

    public NotificationRequest(UserType userType, String userID) {
        this.userType = userType;
        this.userID = userID;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getUserID() {
        return userID;
    }
}
