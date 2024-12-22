package com.example.lms.submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
@Service
public class SubmissionService {
    @Autowired
    private SubmissionRepository submissionRepository;

    public Submission submitAssignment(Submission submission) {
        return submissionRepository.save(submission);
    }

    public List<Submission> getSubmissionsByAssignmentId(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId);
    }
    public Submission addFeedback(Long submissionId, String feedback) {
        Submission submission = submissionRepository.findById(submissionId).orElse(null);
        if (submission != null) {
            submission.setFeedback(feedback); // إضافة الملاحظات
            return submissionRepository.save(submission); // حفظ التغيير
        }
        return null;
    }
    public Submission submitAssignmentWithFile(Submission submission, MultipartFile file) throws IOException {
        // حفظ الملف في المسار المناسب
        String filePath = saveFile(file);
        submission.setFilePath(filePath);  // تعيين مسار الملف
        return submissionRepository.save(submission);  // حفظ التقديم
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path path = Paths.get("uploads/" + fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return path.toString();  // إرجاع المسار الكامل للملف
    }
    public Submission gradeAssignment(Long submissionId, int grade) {
        Submission submission = submissionRepository.findById(submissionId).orElse(null);
        if (submission != null) {
            submission.setGrade(grade); // تعيين التقييم
            return submissionRepository.save(submission); // حفظ التقييم
        }
        return null;
    }
}
