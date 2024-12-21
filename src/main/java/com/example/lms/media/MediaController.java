package com.example.lms.media;

import com.example.lms.auth.JwtService;
import com.example.lms.common.enums.UserRole;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/{lessonId}/upload")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<?> uploadMedia(
            @PathVariable String lessonId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        String instructorId = getInstructorIdFromToken(request);

        try {
            Media media = mediaService.uploadMedia(lessonId, file);
            return new ResponseEntity<>(media, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{lessonId}")
    @RolesAllowed({"STUDENT", "INSTRUCTOR"})
    public ResponseEntity<?> getMediaByLesson(@PathVariable String lessonId) {
        return ResponseEntity.ok(mediaService.getMediaByLesson(lessonId));
    }

    private String getInstructorIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token is missing or invalid");
        }

        String token = authHeader.substring(7);
        String userId = jwtService.extractUsername(token);

        User instructor = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (!instructor.getRole().equals(UserRole.INSTRUCTOR)) {
            throw new RuntimeException("Unauthorized access");
        }

        return userId;
    }
}
