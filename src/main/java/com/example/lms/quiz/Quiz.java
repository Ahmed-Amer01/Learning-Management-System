package com.example.lms.quiz;

import jakarta.persistence.*;
import lombok.*;
import com.example.lms.question.Question;

import java.util.List;

@Entity
@Table(name = "quizzes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private String courseId;

    @Column(nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(
            name = "quiz_questions",
            joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
