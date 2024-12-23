package com.example.lms.question;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, String> {
    Optional<Question> findByCourseIdAndQuestionId(String questionId, String courseId);
    List<Question> findByCourseId(String courseId);

    @Query("SELECT q FROM Question q WHERE q.courseId = :courseId ORDER BY RANDOM()")
    List<Question> findRandomQuestionsByCourseId(@Param("courseId") String courseId, Pageable pageable);
}
