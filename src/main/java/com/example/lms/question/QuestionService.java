package com.example.lms.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public void createQuestion(String courseId, QuestionDTO questionDTO) {
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
        List<Question> questions = questionRepository.findByCourseId(courseId);
        return questions.stream().map(this::convertToDTO).toList();
    }

    public void updateQuestion(String courseId, String questionId, QuestionDTO questionDTO) {
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

    public void deleteQuestion(String courseId, String questionId) {
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
