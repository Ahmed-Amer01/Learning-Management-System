package com.example.lms.course;

import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import com.example.lms.auth.JwtService;
import com.example.lms.common.enums.UserRole;
import com.example.lms.lesson.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseRepository courseService;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService; // Inject JwtService

    // API for students to view available courses
    @GetMapping("/available")
    @RolesAllowed({"STUDENT", "INSTRUCTOR"})
    public  ResponseEntity<?> getAvailableCourses(HttpServletRequest request) {
    	// Extract the JWT token from the Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Token is missing or invalid", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);  // Remove "Bearer " prefix

        // Extract username (user ID) from the token
        String userId = jwtService.extractUsername(token);
        if (userId == null) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        // Find the user by ID
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        List<Course> courses = courseService.findAll();
        
        // If the user is a student, hide the OTP or other sensitive information
        if (user.getRole().equals(UserRole.STUDENT.name())) {
        	 // If the user is a student, remove OTP from lessons
            courses.forEach(course -> {
                course.getLessons().forEach(lesson -> {
                    lesson.setOtp(null);  // Remove OTP for students
                });
            });
        }

        // If the user is an instructor or admin, return full details
        if (user.getRole().equals(UserRole.INSTRUCTOR.name())) {
        	// Return all course details for instructors
            courses = courseService.findAll();
            return ResponseEntity.ok(courses);
        }

        // If the user role is not authorized
        return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
    }
    
    // API for students to enroll in a course
    @RolesAllowed({"STUDENT"})
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<?> enrollInCourse(@PathVariable String courseId, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Token is missing or invalid", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        String userId = jwtService.extractUsername(token);
        if (userId == null) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        User student = userRepository.findById(userId).orElse(null);
        if (student == null) {
            return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
        }

        Course course = courseService.findById(courseId).orElse(null);
        if (course == null) {
            return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
        }
        
        // Check if the user is an student
        if (!student.getRole().equals(UserRole.STUDENT)) {
            return new ResponseEntity<>("Only students can enroll in courses.", HttpStatus.FORBIDDEN);
        }

        course.getStudents().add(student);
        courseService.save(course);
        return new ResponseEntity<>("Enrolled successfully", HttpStatus.OK);
    }
    
    // API for instructors to view enrolled students in their courses
    @GetMapping("/{courseId}/students")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<?> getEnrolledStudents(@PathVariable String courseId, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Token is missing or invalid", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        String userId = jwtService.extractUsername(token);
        if (userId == null) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        User instructor = userRepository.findById(userId).orElse(null);
        if (instructor == null || !instructor.getRole().equals(UserRole.INSTRUCTOR)) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.FORBIDDEN);
        }

        Course course = courseService.findById(courseId).orElse(null);
        if (course == null || !course.getInstructor().equals(instructor)) {
            return new ResponseEntity<>("Course not found or unauthorized", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(course.getStudents(), HttpStatus.OK);
    }
    
    // Endpoint for instructors to create courses
    @PostMapping("/create")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<?> createCourse(@RequestBody CourseDto courseDto, HttpServletRequest request) {
        // Extract the JWT token from the Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Token is missing or invalid", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);  // Remove "Bearer " prefix

        // Extract username (user ID) from the token
        String userId = jwtService.extractUsername(token);
        if (userId == null) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        // Find the user by ID
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Check if the user is an instructor
        if (!user.getRole().equals(UserRole.INSTRUCTOR)) {
            return new ResponseEntity<>("Only instructors can create courses.", HttpStatus.FORBIDDEN);
        }

        // Create the course and associate with the instructor
        Course course = new Course();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setDuration(courseDto.getDuration());
        course.setInstructor(user);

        // Add the course to the instructor's list of courses
        user.getCourses().add(course);

        // Save the course and update the instructor's course list
        courseService.save(course);
        userRepository.save(user);  // Save the instructor with the updated course list

        // Return the created course with a 201 Created status
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }
    
    // API for instructors to add lessons to their courses
    @PostMapping("/{courseId}/add-lesson")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<?> addLesson(@PathVariable String courseId, @RequestBody Lesson lessonRequest, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Token is missing or invalid", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        String userId = jwtService.extractUsername(token);

        User instructor = userRepository.findById(userId).orElse(null);
        Course course = courseService.findById(courseId).orElse(null);
        if (course == null || !course.getInstructor().equals(instructor)) {
            return new ResponseEntity<>("Unauthorized access or course not found", HttpStatus.FORBIDDEN);
        }

        Lesson lesson = new Lesson();
        lesson.setName(lessonRequest.getName());
        lesson.setCourse(course);
        lessonRepository.save(lesson);

        course.getLessons().add(lesson);
        courseService.save(course);

        return new ResponseEntity<>("Lesson added successfully", HttpStatus.OK);
    }
}
