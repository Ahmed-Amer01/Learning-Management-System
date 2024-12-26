package com.example.lms.lesson;

import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import com.example.lms.common.enums.UserRole;
import com.example.lms.course.*;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    // Create a lesson for a course
    public Lesson createLesson(LessonDto createLessonDto, String instructorId) {
    	User instructor = validateUser(instructorId, UserRole.INSTRUCTOR);
    	
        // Validate course
        Course course = courseRepository.findById(createLessonDto.getCourseId())
                		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        // Validate instructor authorization
        if (!course.getInstructor().equals(instructor)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the instructor of this course");
        }

        // Create and save lesson
        Lesson lesson = Lesson.builder()
                .name(createLessonDto.getName())
                .course(course)
                .build();

        return lessonRepository.save(lesson);
    }

    // Generate OTP for a lesson
    public Lesson generateOtp(String lessonId, String instructorId) {
    	User instructor = validateUser(instructorId, UserRole.INSTRUCTOR);
    	
        Lesson lesson = lessonRepository.findById(lessonId)
                		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));

        if (!lesson.getCourse().getInstructor().equals(instructor)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the instructor of this course");
        }

        String otp = String.valueOf((int) (Math.random() * 9000) + 1000); // Generate 4-digit OTP
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10); // OTP valid for 10 minutes
        lesson.setOtp(otp);
        lesson.setOtpExpirationTime(expirationTime);

        return lessonRepository.save(lesson);
    }
    
    public void attendLesson(String lessonId, String otp, String studentId) {
    	User student = validateUser(studentId, UserRole.STUDENT);
    	
        // Retrieve the lesson
        Lesson lesson = lessonRepository.findById(lessonId)
                		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));

        // Validate OTP expiration
        if (lesson.getOtpExpirationTime() == null || LocalDateTime.now().isAfter(lesson.getOtpExpirationTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired");
        }
        
        // Validate OTP
        if (!lesson.getOtp().equals(otp)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }

        // Validate student enrollment in the course
        Course course = lesson.getCourse();
        if (!course.getStudents().contains(student)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not enrolled in this course");
        }

        // Check if attendance is already marked
        if (lesson.getStudentsAttended().contains(student)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attendance already marked for this lesson");
        }

        // Mark attendance
        lesson.getStudentsAttended().add(student);
        lessonRepository.save(lesson);
    }

    private User validateUser(String userId, UserRole requiredRole) {
        User user = userRepository.findById(userId)
                	.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.getRole().equals(requiredRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
        return user;
    }
}
