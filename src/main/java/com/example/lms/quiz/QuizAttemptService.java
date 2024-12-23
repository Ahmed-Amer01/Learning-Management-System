package com.example.lms.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuizAttemptService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

//    @Autowired
//    private StudentService studentService;

    public boolean quizAttempt(String courseId, String studentId, String quizId) {
        Optional<QuizAttempt> attempt = quizAttemptRepository.findQuizAttemptByCourseIdAndStudentIdAndQuizId(courseId, studentId, quizId);
        return attempt.isPresent();
    }


    public QuizAttempt submit(String courseId, String studentId, String quizId, QuizAttemptDTO attemptDTO) {
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setCourseId(courseId);
        quizAttempt.setQuiz(quizRepository.findByCourseIdAndId(courseId, quizId).get());
        quizAttempt.setStudentId(studentId);
        quizAttempt.setAnswers(attemptDTO.getAnswers());
        quizAttempt.setScore(calculateScore(courseId, studentId, quizId, attemptDTO));
        quizAttempt.setAttemptTime(LocalDateTime.now());
        return quizAttemptRepository.save(quizAttempt);
    }


    public int calculateScore(String courseId, String studentId, String quizId, QuizAttemptDTO attemptDTO) {
        int score = 0;
        Optional<Quiz> quiz = quizRepository.findByCourseIdAndId(courseId, quizId);
        if (quiz.isPresent()) {
            for (int i = 0; i < attemptDTO.getAnswers().size(); i++) {
                if (attemptDTO.getAnswers().get(i).toUpperCase().equals(quiz.get().getQuestions().get(i).getAnswer().toUpperCase())) {
                    score++;
                }
            }
        }
        return score;
    }

    public QuizAttemptResultsDTO getResults(String courseId, String studentId, String quizId) {
        QuizAttemptResultsDTO results = new QuizAttemptResultsDTO();
        Quiz quiz = quizRepository.findByCourseIdAndId(courseId, quizId).get();
        Optional<QuizAttempt> quizAttempt = quizAttemptRepository.findQuizAttemptByCourseIdAndStudentIdAndQuizId(courseId, studentId, quizId);
        results.setQuiz(quiz);
        results.setAnswers(quizAttempt.get().getAnswers());
        results.setScore(quizAttempt.get().getScore());
        results.setAttemptTime(quizAttempt.get().getAttemptTime());
        return results;
    }


    public List<QuizAttempt> getQuizzesSubmittedByStudent(String studentId, String courseId) {
        return quizAttemptRepository.findQuizAttemptsByCourseIdAndStudentId(studentId, courseId);
    }


    public double getQuizzesGradeByStudent(String studentId, String courseId) {
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findQuizAttemptsByCourseIdAndStudentId(studentId, courseId);
        double totalScore = 0;
        for (QuizAttempt quizAttempt : quizAttempts) {
            totalScore += quizAttempt.getScore();
        }
        return totalScore;
    }
}
