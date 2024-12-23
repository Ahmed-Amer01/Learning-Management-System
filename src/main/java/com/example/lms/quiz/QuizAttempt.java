package com.example.lms.quiz;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quiz_attempts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @OneToOne
    @JoinColumn(name = "id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String courseId;

    @ElementCollection
    @CollectionTable(
            name = "quiz_answers",
            joinColumns = @JoinColumn(name = "quiz_attempt_id")
    )
    @Column(name = "answer", nullable = false)
    private List<String> answers;

    private int score;

    @Column(nullable = false)
    private LocalDateTime attemptTime;
}
