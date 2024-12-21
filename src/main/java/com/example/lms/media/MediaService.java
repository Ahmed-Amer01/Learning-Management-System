package com.example.lms.media;

import com.example.lms.lesson.Lesson;
import com.example.lms.lesson.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;
    private final LessonRepository lessonRepository;

    @Value("${media.folder}")
    private String mediaFolder;

    public Media uploadMedia(String lessonId, MultipartFile file) throws IOException {
        // Validate lesson
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        // Create the media folder if it doesn't exist
        File folder = new File(mediaFolder);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (!created) {
                throw new IOException("Failed to create media folder");
            }
        }

        // Prepare the filename and destination path
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path destinationPath = Paths.get(folder.getAbsolutePath(), filename);

        try {
            // Copy the file to the destination folder
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationPath);
            }
            // Log success message
            System.out.println("File uploaded to: " + destinationPath.toString());
        } catch (IOException e) {
            // Log the exception details for troubleshooting
            e.printStackTrace();  // For debugging purposes
            throw new IOException("Failed to copy file: " + e.getMessage(), e);
        }

        // Save media details in the database
        Media media = Media.builder()
                .filename(filename)
                .fileType(file.getContentType())
                .filePath(destinationPath.toString())
                .lesson(lesson)
                .build();

        return mediaRepository.save(media);
    }

    public List<Media> getMediaByLesson(String lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        return lesson.getMediaList();
    }
}
