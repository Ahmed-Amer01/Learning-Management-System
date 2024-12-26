package com.example.lms.notification;

import com.example.lms.common.enums.UserRole;
import com.example.lms.Notifications.DTOs.NotificationRequest;
import com.example.lms.Notifications.Enums.NotificationType;
import com.example.lms.Notifications.NotificationCreator.EnrollmentCreator;
import com.example.lms.Notifications.NotificationsManager.Notification;
import com.example.lms.Notifications.NotificationsManager.NotificationController;
import com.example.lms.Notifications.NotificationsManager.NotificationData;
import com.example.lms.Notifications.NotificationsManager.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class NotificationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
        objectMapper = new ObjectMapper(); // Initialize ObjectMapper
    }

    @Test
    void testPostEnrollmentNotification() throws Exception {
        // Register JavaTimeModule to handle LocalDateTime
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Create EnrollmentCreator object
        EnrollmentCreator enrollmentCreator = new EnrollmentCreator("222", "Operating Systems", "3");

        // Create NotificationData using the EnrollmentCreator
        NotificationData notificationData1 = enrollmentCreator.createNewEnrollmentNotification("20220016").get(0);
        NotificationData notificationData2 = enrollmentCreator.createNewEnrollmentNotification("20220016").get(1);

        String createdAt_formatted = "Monday 23/12/2024 7:57 PM";
        Notification notification1 = new Notification(notificationData1, createdAt_formatted, false);
        Notification notification2 = new Notification(notificationData2, createdAt_formatted, false);

        // Mock the behavior of the notificationService
        Mockito.when(notificationService.addNotification(any(NotificationData.class)))
                .thenReturn(notification1, notification2);

        // List of notifications
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        // Do nothing when sending the notification by email
        Mockito.doNothing().when(notificationService).sendNotificationByEmail(any(Notification.class));



        // Perform the post request
        mockMvc.perform(post("/20220016/success")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(enrollmentCreator))) // Serialize to byte array
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notificationID").value("2"))
                .andExpect(jsonPath("$[1].notificationID").value("3"))
                .andExpect(jsonPath("$[0].notificationData.receiverID").value("3"))
                .andExpect(jsonPath("$[1].notificationData.receiverID").value("20220016"));

    }



    @Test
    void testGetNotifications() throws Exception {
        NotificationRequest request = new NotificationRequest(UserRole.STUDENT, "123456", true);
        LocalDateTime now = LocalDateTime.now();
        String createdAt_formatted = "Monday 23/12/2024 7:57 PM";
        NotificationData notificationData = new NotificationData(NotificationType.ASSIGNMENT_GRADED, UserRole.STUDENT, "123456", "assignment 2 has been graded", now);
        Notification notification1 = new Notification(notificationData, createdAt_formatted, true);

        Mockito.when(notificationService.getNotifications(UserRole.STUDENT, "123456", true))
                .thenReturn(List.of(notification1));

        mockMvc.perform(get("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notificationID").value("1"))
                .andExpect(jsonPath("$[0].notificationData.message").value("assignment 2 has been graded"));
    }

    @Test
    void testPatchNotification() throws Exception {
        Mockito.doNothing().when(notificationService).markNotificationAsRead("1");

        mockMvc.perform(patch("/notifications/1"))
                .andExpect(status().isOk());
    }
}
