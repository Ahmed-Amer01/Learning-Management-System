package com.example.lms.question;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private String questionType;
    private String questionText;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> choices;
    private String answer;
}
