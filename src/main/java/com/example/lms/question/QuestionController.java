package com.example.lms.question;


//import com.example.lms.course.CourseService;
//import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequestMapping("/questions")
@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

//    @Autowired
//    private CourseService courseService;

//    @RolesAllowed({"INSTRUCTOR", "STUDENT"})
    @GetMapping("/{courseId}/question-bank")
    public ResponseEntity<?> getAllQuestionsInQuestionBank(@PathVariable String courseId) {

//        if (courseService.getCourseById(courseId).isEmpty()) {
//            return ResponseEntity.status(404).body("Course not found with the given courseId.");
//        }

        List<QuestionDTO> questions = questionService.getAllQuestionsByCourse(courseId);
        return ResponseEntity.ok(questions);
    }


//    @RolesAllowed({"INSTRUCTOR"})
    @PostMapping("/{courseId}/question-bank")
    public ResponseEntity<?> addQuestionToQuestionBank(@PathVariable String courseId,
                                                            @RequestBody @Valid QuestionDTO questionDTO) {

//        if (courseService.getCourseById(courseId).isEmpty()) {
//            return ResponseEntity.status(404).body("Course not found with the given courseId.");
//        }

        questionService.createQuestion(courseId, questionDTO);

        return ResponseEntity.status(201).body("Question added to the question bank successfully!");
    }


//    @RolesAllowed({"INSTRUCTOR"})
    @PutMapping("/{courseId}/question-bank/{questionId}")
    public ResponseEntity<?> updateQuestionInQuestionBank(@PathVariable String courseId,
                                                               @PathVariable String questionId,
                                                               @RequestBody @Valid QuestionDTO questionDTO) {
//        if (courseService.getCourseById(courseId).isEmpty()) {
//            return ResponseEntity.status(404).body("Course not found with the given courseId.");
//        }

        if (questionService.geQuestionByIdAndCourseId(courseId, questionId).isEmpty()) {
            return ResponseEntity.status(404).body("Question not found in the question bank.");
        }

        questionService.updateQuestion(courseId, questionId, questionDTO);
        return ResponseEntity.ok("Question updated successfully in the question bank!");
    }


//    @RolesAllowed({"INSTRUCTOR"})
    @DeleteMapping("/{courseId}/question-bank/{questionId}")
    public ResponseEntity<?> deleteQuestionFromQuestionBank(@PathVariable String courseId,
                                                            @PathVariable String questionId) {
//        if (courseService.getCourseById(courseId).isEmpty()) {
//            return ResponseEntity.status(404).body("Course not found with the given courseId.");
//        }

        if (questionService.geQuestionByIdAndCourseId(courseId, questionId).isEmpty()) {
            return ResponseEntity.status(404).body("Question not found in the question bank.");
        }

        questionService.deleteQuestion(courseId, questionId);
        return ResponseEntity.ok("Question deleted successfully from the question bank!");
    }
}
