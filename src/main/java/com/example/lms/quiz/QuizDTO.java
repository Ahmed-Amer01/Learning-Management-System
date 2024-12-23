package com.example.lms.quiz;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizDTO {
        private String title;
        private List<QuestionForQuizDTO> questions;
}
