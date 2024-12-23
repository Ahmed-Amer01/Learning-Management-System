package com.example.demo.OTP;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;


@Entity
@Table(name = "otp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OTP  {
        @Id
        @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
        private String OTP_ID;

        @Column(nullable = false)
        private String courseID;
        @Column(nullable = false)
        private String lessonID;
        @Column(nullable = false)
        private String format;
        @Column(nullable = false)
        private LocalDateTime expirationTime;


        private static final AtomicLong counter = new AtomicLong();

        public OTP(String courseID, String lessonID, String format, LocalDateTime expirationTime) {
            this.OTP_ID = String.valueOf(counter.incrementAndGet());
            this.courseID = courseID;
            this.lessonID = lessonID;
            this.format = format;
            this.expirationTime = expirationTime;
        }
}
