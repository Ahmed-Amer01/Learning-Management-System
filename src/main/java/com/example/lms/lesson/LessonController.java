package com.example.lms.lesson;

import com.example.lms.auth.JwtService;
import com.example.lms.common.enums.UserRole;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    // Endpoint to create a lesson
    @PostMapping("/create")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<?> createLesson(@RequestBody LessonDto createLessonDto, HttpServletRequest request) {
        String instructorId = getInstructorIdFromToken(request);

        Lesson lesson = lessonService.createLesson(createLessonDto, instructorId);

        return new ResponseEntity<>(lesson, HttpStatus.CREATED);
    }

    // Endpoint to generate OTP for a lesson
    @PostMapping("/{lessonId}/generate-otp")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<?> generateOtp(@PathVariable String lessonId, HttpServletRequest request) {
        String instructorId = getInstructorIdFromToken(request);

        Lesson updatedLesson = lessonService.generateOtp(lessonId, instructorId);

        return ResponseEntity.ok(updatedLesson);
    }

    // Helper method to extract instructor ID from JWT token
    private String getInstructorIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token");
        }

        String token = authHeader.substring(7);
        String instructorId = jwtService.extractUsername(token);

        if (instructorId == null || userRepository.findById(instructorId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        return instructorId;
    }
    
    @PostMapping("/{lessonId}/attend")
    @RolesAllowed({"STUDENT"})
    public ResponseEntity<?> attendLesson(@PathVariable String lessonId, @RequestBody String otp, HttpServletRequest request) {
        // Extract the authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Token is missing or invalid", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        String userId = jwtService.extractUsername(token);

        // Validate the student (check if user is a student)
        User student = userRepository.findById(userId).orElse(null);
        if (student == null || !student.getRole().equals(UserRole.STUDENT)) {
            return new ResponseEntity<>("Invalid or unauthorized student", HttpStatus.FORBIDDEN);
        }

        // Check if the lesson exists
        Lesson lesson = lessonService.getLessonById(lessonId);
        if (lesson == null) {
            return new ResponseEntity<>("Lesson not found", HttpStatus.NOT_FOUND);
        }

        // Validate OTP
        if (!lesson.getOtp().equals(otp)) {
            return new ResponseEntity<>("Invalid OTP", HttpStatus.FORBIDDEN);
        }

        // Attempt to mark attendance
        try {
            lessonService.attendLesson(lessonId, otp, student);
            return new ResponseEntity<>("Attendance marked successfully", HttpStatus.OK);
        } catch (ResponseStatusException ex) {
            return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
        }
    }

}
