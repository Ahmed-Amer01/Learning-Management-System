package com.example.lms.quiz;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuizAttemptResultsDTO {
    private List<String> answers;
    private Quiz quiz;
    private int score;
    private LocalDateTime attemptTime;
}
