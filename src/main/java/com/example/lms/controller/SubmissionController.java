package com.example.lms.controller;

import com.example.lms.model.Submission;
import com.example.lms.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/assignments/{assignmentId}/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    // 1. تقديم الواجب
    @PostMapping
    public ResponseEntity<Submission> submitAssignment(@PathVariable Long courseId, @PathVariable Long assignmentId, @RequestBody Submission submission) {
        submission.setAssignmentId(assignmentId); // تعيين assignmentId للـSubmission
        Submission createdSubmission = submissionService.submitAssignment(submission); // تقديم الواجب
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubmission); // إرجاع الـSubmission مع حالة CREATED
    }

    // 2. عرض جميع التقديمات لواجب معين
    @GetMapping
    public ResponseEntity<List<Submission>> getSubmissionsByAssignmentId(@PathVariable Long courseId, @PathVariable Long assignmentId) {
        List<Submission> submissions = submissionService.getSubmissionsByAssignmentId(assignmentId); // جلب جميع التقديمات لواجب معين
        return ResponseEntity.ok(submissions); // إرجاع التقديمات
    }

    // 3. إضافة ملاحظات على تقديم معين
    @PostMapping("/{submissionId}/feedback")
    public ResponseEntity<Submission> addFeedback(@PathVariable Long courseId, @PathVariable Long assignmentId, @PathVariable Long submissionId, @RequestBody String feedback) {
        Submission submissionWithFeedback = submissionService.addFeedback(submissionId, feedback);
        if (submissionWithFeedback == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // إذا كان الـSubmission مش موجود
        }
        return ResponseEntity.ok(submissionWithFeedback); // إرجاع الـSubmission مع الملاحظات
    }
    @PostMapping("/submit")
    public ResponseEntity<Submission> submitAssignment(
            @PathVariable Long courseId,
            @PathVariable Long assignmentId,
            @RequestParam("file") MultipartFile file,
            @RequestBody Submission submission) throws IOException {
        submission.setAssignmentId(assignmentId);
        Submission createdSubmission = submissionService.submitAssignmentWithFile(submission, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubmission);
    }
    @PostMapping("/{submissionId}/grade")
    public ResponseEntity<Submission> gradeAssignment(
            @PathVariable Long courseId,
            @PathVariable Long assignmentId,
            @PathVariable Long submissionId,
            @RequestBody int grade) {

        Submission gradedSubmission = submissionService.gradeAssignment(submissionId, grade);
        if (gradedSubmission == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(gradedSubmission);
    }
}
