package com.example.lms.quiz;

import com.example.lms.question.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    public Quiz createQuiz(String courseId, String title, int questionCount) {
        Pageable pageable = PageRequest.of(0, questionCount);
        List<Question> questions = questionRepository.findRandomQuestionsByCourseId(courseId, pageable);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setCourseId(courseId);
        quiz.setQuestions(questions);
        return quizRepository.save(quiz);
    }

    public void deleteQuiz(String courseId, String quizId) {
        quizRepository.deleteByCourseIdAndId(courseId, quizId);
    }


    public Optional<Quiz> getQuizForInstructor(String courseId, String quizId) {
        Optional<Quiz> quiz = quizRepository.findByCourseIdAndId(courseId, quizId);
        quiz.ifPresent(q -> q.getQuestions().forEach(question -> {
            if (!"MCQ".equals(question.getQuestionType())) {
                question.setChoices(null);
            }
        }));
        return quiz;
    }


    public QuizDTO getQuizForStudent(String courseId, String quizId) {
        Optional<Quiz> quiz = getQuizForInstructor(courseId, quizId);
        List<QuestionForQuizDTO> publicQuestions = quiz.get().getQuestions().stream()
                .map(q -> new QuestionForQuizDTO(q.getQuestionType(), q.getQuestionText(), (q.getQuestionType().equals("MCQ") ? q.getChoices() : null)))
                .collect(Collectors.toList());

        return new QuizDTO(quiz.get().getTitle(), publicQuestions);
    }
}
