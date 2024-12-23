package com.example.lms.quiz;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizzes/{courseId}")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizAttemptService quizAttemptService;

//    @RolesAllowed({"INSTRUCTOR"})
    @PostMapping
    public ResponseEntity<?> createQuiz(@PathVariable String courseId, @RequestParam String title, @RequestParam int questionCount) {

        //        if (courseService.getCourseById(courseId).isEmpty()) {
//            return ResponseEntity.status(404).body("Course not found with the given courseId.");
//        }


        Quiz quiz = quizService.createQuiz(courseId, title, questionCount);
        return ResponseEntity.ok("Quiz created successfully with ID: " + quiz.getId());
    }

//    @RolesAllowed({"INSTRUCTOR"})
    @DeleteMapping("/{quizId}")
    public ResponseEntity<?> deleteQuiz(@PathVariable String courseId, @PathVariable String quizId) {

        //        if (courseService.getCourseById(courseId).isEmpty()) {
//            return ResponseEntity.status(404).body("Course not found with the given courseId.");
//        }


        if (quizService.getQuizForInstructor(courseId, quizId).isEmpty()) {
            return ResponseEntity.status(404).body("Quiz with ID: " + quizId + " does not exist");
        }

        quizService.deleteQuiz(courseId, quizId);
        return ResponseEntity.ok("Quiz deleted successfully");
    }

//    @RolesAllowed({"INSTRUCTOR"})
    @GetMapping("/{quizId}")
    public ResponseEntity<?> getQuizForInstructor(@PathVariable String courseId, @PathVariable String quizId) {

        //        if (courseService.getCourseById(courseId).isEmpty()) {
//            return ResponseEntity.status(404).body("Course not found with the given courseId.");
//        }


        if (quizService.getQuizForInstructor(courseId, quizId).isEmpty()) {
            return ResponseEntity.status(404).body("Quiz with ID: " + quizId + " does not exist");
        }

        return ResponseEntity.ok(quizService.getQuizForInstructor(courseId, quizId));
    }

//    @RolesAllowed({"Student"})
    @GetMapping("/{quizId}/students")
    public ResponseEntity<?> getQuizForStudent(@PathVariable String courseId, @PathVariable String quizId) {

        //        if (courseService.getCourseById(courseId).isEmpty()) {
//            return ResponseEntity.status(404).body("Course not found with the given courseId.");
//        }


        if (quizService.getQuizForInstructor(courseId, quizId).isEmpty()) {
            return ResponseEntity.status(404).body("Quiz with ID: " + quizId + " does not exist");
        }

        return ResponseEntity.ok(quizService.getQuizForStudent(courseId, quizId));
    }

    @PostMapping("/{quizId}/students/{studentId}")
    public ResponseEntity<?> startQuizForStudent(@PathVariable String courseId, @PathVariable String quizId, @PathVariable String studentId, @RequestBody QuizAttemptDTO attemptDTO) {
        if (quizService.getQuizForInstructor(courseId, quizId).isEmpty()) {
            return ResponseEntity.status(404).body("Quiz with ID: " + quizId + " does not exist");
        }

        //        if (studentService.getStduentById(studentId).isEmpty()) {
//            return ResponseEntity.status(404).body("Student not found with the given studentId.");
//        }

        //        if (courseService.getCourseById(courseId).isEmpty()) {
//            return ResponseEntity.status(404).body("Course not found with the given courseId.");
//        }


        if (quizAttemptService.quizAttempt(courseId, studentId, quizId)) {
            return ResponseEntity.status(404).body("You have already attempted this quiz.");
        }

        quizAttemptService.submit(courseId, studentId, quizId, attemptDTO);
        return ResponseEntity.ok(quizAttemptService.getResults(courseId, studentId, quizId));
    }


    @GetMapping("/{quizId}/students/{studentId}/result")
    public ResponseEntity<?> showQuizResultForStudent(@PathVariable String courseId, @PathVariable String quizId, @PathVariable String studentId) {
        if (quizService.getQuizForInstructor(courseId, quizId).isEmpty()) {
            return ResponseEntity.status(404).body("Quiz with ID: " + quizId + " does not exist");
        }

        if (!quizAttemptService.quizAttempt(courseId, studentId, quizId)) {
            return ResponseEntity.status(404).body("You haven't attempted the quiz yet.");
        }

        //        if (studentService.getStudentById(studentId).isEmpty()) {
//            return ResponseEntity.status(404).body("Student not found with the given studentId.");
//        }


        //        if (courseService.getCourseById(courseId).isEmpty()) {
//            return ResponseEntity.status(404).body("Course not found with the given courseId.");
//        }


        return ResponseEntity.ok(quizAttemptService.getResults(courseId, studentId, quizId));
    }
}
