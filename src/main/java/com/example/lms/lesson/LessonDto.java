package com.example.lms.lesson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private String name;
    private String courseId; // ID of the course to which the lesson belongs
}
