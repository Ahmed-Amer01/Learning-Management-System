package com.example.lms.lesson;

import com.example.lms.auth.JwtService;
import com.example.lms.user.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
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
    	String instructorId = extractUserId(request);
        return new ResponseEntity<>(lessonService.createLesson(createLessonDto, instructorId), HttpStatus.CREATED);
    }

    // Endpoint to generate OTP for a lesson
    @PostMapping("/{lessonId}/generate-otp")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<?> generateOtp(@PathVariable String lessonId, HttpServletRequest request) {
        String instructorId = extractUserId(request);
        return ResponseEntity.ok(lessonService.generateOtp(lessonId, instructorId));
    }
    
    @PostMapping("/{lessonId}/attend")
    @RolesAllowed({"STUDENT"})
    public ResponseEntity<?> attendLesson(@PathVariable String lessonId, @RequestBody String otp, HttpServletRequest request) {
    	String studentId = extractUserId(request);
    	
    	lessonService.attendLesson(lessonId, otp, studentId);
        return new ResponseEntity<>("Attendance marked successfully", HttpStatus.OK);
    }
    
    // Method to extract user ID from JWT token
    private String extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
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

}