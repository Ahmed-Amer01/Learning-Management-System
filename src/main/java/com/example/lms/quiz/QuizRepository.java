package com.example.lms.quiz;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {
    Optional<Quiz> findByCourseIdAndId(String courseId, String id);

    @Modifying
    @Transactional
    void deleteByCourseIdAndId(String courseId, String id);
}
