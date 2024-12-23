package com.example.lms.assignment;

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

    // 1. إنشاء واجب جديد
    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@PathVariable String courseId, @RequestBody Assignment assignment) {
        Assignment createdAssignment = assignmentService.createAssignment(courseId, assignment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAssignment);
    }

    // 2. عرض جميع الواجبات في الكورس
    @GetMapping
    public ResponseEntity<List<Assignment>> getAssignmentsByCourseId(@PathVariable String courseId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByCourseId(courseId);
        return ResponseEntity.ok(assignments);
    }
}
