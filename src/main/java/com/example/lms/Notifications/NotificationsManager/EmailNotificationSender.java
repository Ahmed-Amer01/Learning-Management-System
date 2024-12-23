package com.example.lms.Notifications.NotificationsManager;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSender {
    private final JavaMailSender mailSender;
    private final String senderEmailAddress = "professional17771@gmail.com";

    EmailNotificationSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String receiverEmailAddress, String subject, Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmailAddress);
        message.setTo(receiverEmailAddress);
        Date createdAt_date = Date.from(notification.getNotificationData().getCreatedAt()
                .atZone(ZoneId.systemDefault()).toInstant());
        message.setSentDate(createdAt_date);
        message.setSubject(subject);
        message.setText(notification.getNotificationData().getMessage());

        mailSender.send(message);
    }

}
