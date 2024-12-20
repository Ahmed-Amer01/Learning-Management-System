package com.example.demo.OTP;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Entity
public record OTP(@Id
                  @GeneratedValue(strategy = GenerationType.IDENTITY)
                  String OTP_ID,

                  String courseID,
                  String lessonID,
                  String format,
                  LocalDateTime expirationTime
){

    private static final AtomicLong counter = new AtomicLong();

    public OTP(
            String courseID,
            String lessonID,
            String format,
            LocalDateTime expirationTime
    ) {
        this(String.valueOf(counter.incrementAndGet()), courseID, lessonID, format, expirationTime);
    }
}
