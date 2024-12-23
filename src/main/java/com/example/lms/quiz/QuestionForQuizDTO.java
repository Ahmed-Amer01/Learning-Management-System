package com.example.lms.quiz;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionForQuizDTO {
    private String questionText;
    private String questionType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> choices;
}
