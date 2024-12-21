package com.example.lms.lesson;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, String> {
    Optional<Lesson> findByIdAndCourse_Id(String lessonId, String courseId);
}