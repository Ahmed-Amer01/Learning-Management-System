package com.example.lms.question;


import com.example.lms.auth.JwtService;
import com.example.lms.course.CourseService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Validated
@RequestMapping("/questions")
@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;


    @Autowired
    private JwtService jwtService;


    @GetMapping("/{courseId}/question-bank")
    public ResponseEntity<?> getAllQuestionsInQuestionBank(@PathVariable String courseId) {

        List<QuestionDTO> questions = questionService.getAllQuestionsByCourse(courseId);
        return ResponseEntity.ok(questions);
    }


    @RolesAllowed({"INSTRUCTOR"})
    @PostMapping("/{courseId}/question-bank")
    public ResponseEntity<?> addQuestionToQuestionBank(@PathVariable String courseId,
                                                            @RequestBody @Valid QuestionDTO questionDTO,
                                                       HttpServletRequest request) {


        String userId = extractUserId(request);

        questionService.createQuestion(courseId, questionDTO, userId);

        return ResponseEntity.status(201).body("Question added to the question bank successfully!");
    }


    @RolesAllowed({"INSTRUCTOR"})
    @PutMapping("/{courseId}/question-bank/{questionId}")
    public ResponseEntity<?> updateQuestionInQuestionBank(@PathVariable String courseId,
                                                               @PathVariable String questionId,
                                                               @RequestBody @Valid QuestionDTO questionDTO,
                                                          HttpServletRequest request) {

        String userId = extractUserId(request);


        questionService.updateQuestion(courseId, questionId, questionDTO, userId);
        return ResponseEntity.ok("Question updated successfully in the question bank!");
    }


    @RolesAllowed({"INSTRUCTOR"})
    @DeleteMapping("/{courseId}/question-bank/{questionId}")
    public ResponseEntity<?> deleteQuestionFromQuestionBank(@PathVariable String courseId,
                                                            @PathVariable String questionId,
                                                            HttpServletRequest request) {

        String userId = extractUserId(request);


        questionService.deleteQuestion(courseId, questionId, userId);
        return ResponseEntity.ok("Question deleted successfully from the question bank!");
    }

    private String extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token");
        }
        String token = authHeader.substring(7);
        String userId = jwtService.extractUsername(token);

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token");
        }

        return userId;
    }
}
