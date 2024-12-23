package com.example.lms.lesson;

import lombok.Data;

@Data
public class LessonDto {
    private String name;
    private String courseId; // ID of the course to which the lesson belongs
}
