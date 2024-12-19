package com.example.lms.controller;

import com.example.lms.model.Assignment;
import com.example.lms.model.Submission;
import com.example.lms.service.AssignmentService;
import com.example.lms.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SubmissionService submissionService;

    // 1. إنشاء واجب جديد
    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@PathVariable Long courseId, @RequestBody Assignment assignment) {
        assignment.setCourseId(courseId); // تعيين courseId للـAssignment
        Assignment createdAssignment = assignmentService.createAssignment(assignment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAssignment); // إرجاع الـAssignment مع حالة CREATED
    }

    // 2. عرض جميع الواجبات في الكورس
    @GetMapping
    public ResponseEntity<List<Assignment>> getAssignmentsByCourseId(@PathVariable Long courseId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByCourseId(courseId); // جلب جميع الواجبات للكورس
        return ResponseEntity.ok(assignments); // إرجاع الواجبات
    }

    // 3. تقديم الواجب
    /*
    // 4. عرض جميع التقديمات (submissions) لواجب معين
    @GetMapping("/{assignmentId}/submissions")
    public ResponseEntity<List<Submission>> getSubmissionsByAssignmentId(@PathVariable Long courseId, @PathVariable Long assignmentId) {
        List<Submission> submissions = submissionService.getSubmissionsByAssignmentId(assignmentId); // جلب جميع التقديمات لواجب معين
        return ResponseEntity.ok(submissions); // إرجاع التقديمات
    }
     */
}
