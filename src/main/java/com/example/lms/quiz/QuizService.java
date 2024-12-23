package com.example.lms.quiz;

import com.example.lms.common.enums.UserRole;
import com.example.lms.course.Course;
import com.example.lms.course.CourseRepository;
import com.example.lms.question.*;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public Quiz createQuiz(String courseId, String title, int questionCount, String userId) {

        if (courseRepository.findById(courseId).isEmpty()) {
            throw new RuntimeException("Course not found with the given courseId.");
        }

       User user = validateUser(userId);

       if (!user.getRole().equals(UserRole.INSTRUCTOR)) {
           throw new RuntimeException("Only instructor can create Quiz");
       }

        Pageable pageable = PageRequest.of(0, questionCount);
        List<Question> questions = questionRepository.findRandomQuestionsByCourseId(courseId, pageable);

//        Course course = courseRepository.findById(courseId).get();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
//        quiz.setCourse(course);
        quiz.setCourseId(courseId);
        quiz.setQuestions(questions);
        return quizRepository.save(quiz);
    }

    private User validateUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteQuiz(String courseId, String quizId, String userId) {

        if (courseRepository.findById(courseId).isEmpty()) {
            throw new RuntimeException("Course not found with the given courseId.");
        }

        User user = validateUser(userId);
        if (!user.getRole().equals(UserRole.INSTRUCTOR)) {
            throw new RuntimeException("Only instructor can remove Quiz");
        }

        if (quizRepository.findByCourseIdAndId(courseId, quizId).isEmpty()) {
            throw new RuntimeException("Quiz with ID: " + quizId + " does not exist");
        }

        quizRepository.deleteByCourseIdAndId(courseId, quizId);
    }


    public Optional<Quiz> getQuizForInstructor(String courseId, String quizId, String userId) {

        if (courseRepository.findById(courseId).isEmpty()) {
            throw new RuntimeException("Course not found with the given courseId.");
        }

        User user = validateUser(userId);
        if (!user.getRole().equals(UserRole.INSTRUCTOR)) {
            throw new RuntimeException("Only instructor can get the Quiz with answers");
        }


        if (quizRepository.findByCourseIdAndId(courseId, quizId).isEmpty()) {
            throw new RuntimeException("Quiz with ID: " + quizId + " does not exist");
        }

        Optional<Quiz> quiz = quizRepository.findByCourseIdAndId(courseId, quizId);
        quiz.ifPresent(q -> q.getQuestions().forEach(question -> {
            if (!"MCQ".equals(question.getQuestionType())) {
                question.setChoices(null);
            }
        }));
        return quiz;
    }


    public QuizDTO getQuizForStudent(String courseId, String quizId, String userId) {

        if (courseRepository.findById(courseId).isEmpty()) {
            throw new RuntimeException("Course not found with the given courseId.");
        }

        User user = validateUser(userId);
        if (!user.getRole().equals(UserRole.STUDENT)) {
            throw new RuntimeException("Only students can get the Quiz without answers");
        }


        if (quizRepository.findByCourseIdAndId(courseId, quizId).isEmpty()) {
            throw new RuntimeException("Quiz with ID: " + quizId + " does not exist");
        }

        Optional<Quiz> quiz = quizRepository.findByCourseIdAndId(courseId, quizId);
        List<QuestionForQuizDTO> publicQuestions = quiz.get().getQuestions().stream()
                .map(q -> new QuestionForQuizDTO(q.getQuestionType(), q.getQuestionText(), (q.getQuestionType().equals("MCQ") ? q.getChoices() : null)))
                .collect(Collectors.toList());

        return new QuizDTO(quiz.get().getTitle(), publicQuestions);
    }
}
