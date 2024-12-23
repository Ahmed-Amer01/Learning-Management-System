package com.example.lms.question;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "questions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String questionId;

    @Column(nullable = false)
    private String questionType;

    @Column(nullable = false)
    private String questionText;

    @ElementCollection
    @CollectionTable(name = "choices", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "choice")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> choices;

    @Column(nullable = false)
    private String answer;

    // Many-to-One: Many questions belong to a single course
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
