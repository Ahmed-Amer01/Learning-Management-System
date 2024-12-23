package com.example.lms.question;

import com.example.lms.common.enums.UserRole;
import com.example.lms.course.CourseRepository;
import com.example.lms.user.User;
import com.example.lms.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public void createQuestion(String courseId, QuestionDTO questionDTO, String userId) {

        if (courseRepository.findById(courseId).isEmpty()) {
            throw new RuntimeException("Course not found with the given courseId.");
        }

        User user = userRepository.findById(userId).get();
        if (!user.getRole().equals(UserRole.INSTRUCTOR)) {
            throw new RuntimeException("Only Instructor can create a question.");
        }

        Question question = new Question();
        question.setCourseId(courseId);
        question.setQuestionType(questionDTO.getQuestionType());
        question.setQuestionText(questionDTO.getQuestionText());
        question.setChoices(questionDTO.getChoices());
        if (!question.getQuestionType().equals("MCQ")) {
            question.setChoices(null);
        }
        question.setAnswer(questionDTO.getAnswer());
        questionRepository.save(question);
    }

    public List<QuestionDTO> getAllQuestionsByCourse(String courseId) {

        if (courseRepository.findById(courseId).isEmpty()) {
            throw new RuntimeException("Course not found with the given courseId.");
        }

        List<Question> questions = questionRepository.findByCourseId(courseId);
        return questions.stream().map(this::convertToDTO).toList();
    }


    private User validateUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    public void updateQuestion(String courseId, String questionId, QuestionDTO questionDTO, String userId) {

        if (courseRepository.findById(courseId).isEmpty()) {
            throw new RuntimeException("Course not found with the given courseId.");
        }

        User user = validateUser(userId);

        if (!user.getRole().equals(UserRole.INSTRUCTOR)) {
            throw new RuntimeException("Only Instructor can update a question.");
        }

        if (geQuestionByIdAndCourseId(courseId, questionId).isEmpty()) {
            throw new RuntimeException("Question not found in the question bank.");
        }

        Optional<Question> existingQuestion = questionRepository.findByCourseIdAndQuestionId(questionId, courseId);
        Question question = existingQuestion.get();
        question.setQuestionType(questionDTO.getQuestionType());
        question.setQuestionText(questionDTO.getQuestionText());
        question.setChoices(questionDTO.getChoices());
        if (!question.getQuestionType().equals("MCQ")) {
            question.setChoices(null);
        }
        question.setAnswer(questionDTO.getAnswer());
        questionRepository.save(question);
    }

    public void deleteQuestion(String courseId, String questionId, String userId) {

        if (courseRepository.findById(courseId).isEmpty()) {
            throw new RuntimeException("Course not found with the given courseId.");
        }

        User user = validateUser(userId);

        if (!user.getRole().equals(UserRole.INSTRUCTOR)) {
            throw new RuntimeException("Only Instructor can delete a question.");
        }

        if (geQuestionByIdAndCourseId(courseId, questionId).isEmpty()) {
            throw new RuntimeException("Question not found in the question bank.");
        }

        Optional<Question> existingQuestion = questionRepository.findByCourseIdAndQuestionId(questionId, courseId);
        questionRepository.delete(existingQuestion.get());
    }


    public Optional<Question> geQuestionByIdAndCourseId(String courseId, String questionId) {
        return questionRepository.findByCourseIdAndQuestionId(questionId, courseId);
    }

    private QuestionDTO convertToDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setQuestionType(question.getQuestionType());
        dto.setQuestionText(question.getQuestionText());
        dto.setChoices(question.getChoices());
        if (!dto.getQuestionType().equals("MCQ")) {
            dto.setChoices(null);
        }
        dto.setAnswer(question.getAnswer());
        return dto;
    }
}
