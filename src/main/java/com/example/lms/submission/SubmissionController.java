package com.example.lms.submission;

import com.example.lms.auth.JwtService;
import com.example.lms.common.enums.UserRole;
import com.example.lms.course.CourseService;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.lms.course.Course;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses/{courseId}/assignments/{assignmentId}/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService; // Inject JwtService
    @Autowired
    private CourseService courseService;

    // 1. تقديم الواجب
    @PostMapping
    public ResponseEntity<?> submitAssignment(@PathVariable String courseId, @PathVariable Long assignmentId, @RequestBody SubmissionDto submissionDto, HttpServletRequest request) {
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

        if (user.getRole().equals(UserRole.STUDENT)) {
            Optional<Course> optionalCourse = courseService.getCourseById(courseId);
            if (optionalCourse.isPresent()) {
                Course course = optionalCourse.get();
                List<User> courseStudents = course.getStudents();
                boolean isUserEnrolled = courseStudents.stream().anyMatch(student -> student.equals(user));

                if (isUserEnrolled) {
                    Submission submission = new Submission();
                    submission.setFilePath(submissionDto.getFilePath());
                    submission.setSubmittedDate(LocalDateTime.now());
                    submission.setAssignmentId(assignmentId);
                    submission.setStudentId(userId); // حفظ الـ userId كـ String (UUID)

                    Submission createdSubmission = submissionService.submitAssignment(submission); // تقديم الواجب
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdSubmission); // إرجاع الـ Submission مع حالة CREATED
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not enrolled");
                }
            }
        }
        return new ResponseEntity<>("User role not supported", HttpStatus.FORBIDDEN);
    }

    // 2. عرض جميع التقديمات لواجب معين
    @GetMapping
    public ResponseEntity<List<Submission>> getSubmissionsByAssignmentId(@PathVariable Long courseId, @PathVariable Long assignmentId) {
        List<Submission> submissions = submissionService.getSubmissionsByAssignmentId(assignmentId); // جلب جميع التقديمات لواجب معين
        return ResponseEntity.ok(submissions); // إرجاع التقديمات
    }

    // 3. إضافة ملاحظات على تقديم معين (لـ Instructor فقط)
    @PostMapping("/{submissionId}/feedback")
    public ResponseEntity<?> addFeedback(
            @PathVariable Long submissionId,
            @RequestBody String feedback,
            HttpServletRequest request) {

        // 1. Extract JWT Token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Token is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);

        // 2. Extract user ID from token
        String userId = jwtService.extractUsername(token);
        if (userId == null) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        // 3. Check if user exists
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // 4. Check if user is an INSTRUCTOR
        if (!user.getRole().equals(UserRole.INSTRUCTOR)) {
            return new ResponseEntity<>("User is not authorized to add feedback", HttpStatus.FORBIDDEN);
        }

        // 5. Add feedback to the submission
        Submission submissionWithFeedback = submissionService.addFeedback(submissionId, feedback);
        if (submissionWithFeedback == null) {
            return new ResponseEntity<>("Submission not found", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(submissionWithFeedback);
    }

    // 4. إضافة الدرجات على واجب معين (لـ Instructor فقط)
    @PostMapping("/{submissionId}/grade")
    public ResponseEntity<?> gradeAssignment(
            @PathVariable Long submissionId,
            @RequestBody String grade,  // تعديل هنا ليكون String بدل int
            HttpServletRequest request) {

        // 1. استخراج الـ JWT token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Token is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);

        // 2. استخراج ID المستخدم من الـ token
        String userId = jwtService.extractUsername(token);
        if (userId == null) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        // 3. التحقق من وجود المستخدم
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // 4. التحقق من أن المستخدم هو `INSTRUCTOR`
        if (!user.getRole().equals(UserRole.INSTRUCTOR)) {
            return new ResponseEntity<>("User is not authorized to grade assignments", HttpStatus.FORBIDDEN);
        }

        // 5. تعيين الدرجة للتقديم
        Submission gradedSubmission = submissionService.gradeAssignment(submissionId, grade);  // تمرير الـ grade كـ String
        if (gradedSubmission == null) {
            return new ResponseEntity<>("Submission not found", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(gradedSubmission);  // إرجاع الـ Submission المعدلة مع الدرجة
    }

}
