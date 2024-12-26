package com.example.lms.quiz;

import com.example.lms.auth.JwtService;
import com.example.lms.common.enums.UserRole;
import com.example.lms.user.User;
import com.example.lms.course.Course;
import com.example.lms.course.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class QuizControllerTest {

    @Mock
    private QuizService quizService;

    @Mock
    private JwtService jwtService;
    
    @Mock
    private QuizAttemptService quizAttemptService;  // Mock the service

    @Mock
    private QuizAttemptRepository quizAttemptRepository;
    
    @InjectMocks
    private QuizController quizController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();
    }

    @Test
    void testCreateQuiz() throws Exception {
        String userId = "instructor123";
        String courseId = "course123";

        Quiz quiz = new Quiz();
        quiz.setId("quiz123");
        quiz.setTitle("Quiz Title");
        quiz.setCourseId(courseId);
        quiz.setQuestions(Collections.emptyList());

        when(jwtService.extractUsername(anyString())).thenReturn(userId);
        when(quizService.createQuiz(courseId, "Quiz Title", 10, userId)).thenReturn(quiz);

        mockMvc.perform(post("/quizzes/{courseId}", courseId)
                .header("Authorization", "Bearer validToken")
                .param("title", "Quiz Title")
                .param("questionCount", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Quiz created successfully with ID: quiz123"));

        verify(quizService, times(1)).createQuiz(courseId, "Quiz Title", 10, userId);
    }

    @Test
    void testDeleteQuiz() throws Exception {
        String userId = "instructor123";
        String courseId = "course123";
        String quizId = "quiz123";

        doNothing().when(quizService).deleteQuiz(courseId, quizId, userId);
        when(jwtService.extractUsername(anyString())).thenReturn(userId);

        mockMvc.perform(delete("/quizzes/{courseId}/{quizId}", courseId, quizId)
                .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("Quiz deleted successfully"));

        verify(quizService, times(1)).deleteQuiz(courseId, quizId, userId);
    }

    @Test
    void testGetQuizForInstructor() throws Exception {
        String userId = "instructor123";
        String courseId = "course123";
        String quizId = "quiz123";

        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setTitle("Quiz Title");

        when(jwtService.extractUsername(anyString())).thenReturn(userId);
        when(quizService.getQuizForInstructor(courseId, quizId, userId)).thenReturn(java.util.Optional.of(quiz));

        mockMvc.perform(get("/quizzes/{courseId}/{quizId}", courseId, quizId)
                .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(quizId))
                .andExpect(jsonPath("$.title").value("Quiz Title"));

        verify(quizService, times(1)).getQuizForInstructor(courseId, quizId, userId);
    }

    @Test
    void testGetQuizForStudent() throws Exception {
        String userId = "student123";
        String courseId = "course123";
        String quizId = "quiz123";

        QuizDTO quizDTO = new QuizDTO("Quiz Title", Collections.emptyList());

        when(jwtService.extractUsername(anyString())).thenReturn(userId);
        when(quizService.getQuizForStudent(courseId, quizId, userId)).thenReturn(quizDTO);

        mockMvc.perform(get("/quizzes/{courseId}/{quizId}/students", courseId, quizId)
                .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Quiz Title"));

        verify(quizService, times(1)).getQuizForStudent(courseId, quizId, userId);
    }

    @Test
    void testStartQuizForStudent() throws Exception {
        String userId = "student123";
        String courseId = "course123";
        String quizId = "quiz123";
        String studentId = "student123";

        // Create the QuizAttemptDTO with a list of answers
        QuizAttemptDTO attemptDTO = new QuizAttemptDTO();
        attemptDTO.setAnswers(List.of("answer"));

        // Create the mock QuizAttempt object
        QuizAttempt mockQuizAttempt = new QuizAttempt();
        mockQuizAttempt.setCourseId(courseId);
        mockQuizAttempt.setStudentId(studentId);
        mockQuizAttempt.setQuiz(new Quiz());
        mockQuizAttempt.setAnswers(attemptDTO.getAnswers());
        mockQuizAttempt.setScore(1);
        mockQuizAttempt.setAttemptTime(LocalDateTime.now());

        // Mock the save method of quizAttemptRepository to return the mock object
        when(quizAttemptRepository.save(any(QuizAttempt.class))).thenReturn(mockQuizAttempt);

        // Mock the getResults method to return the mock results DTO
        QuizAttemptResultsDTO mockResultsDTO = new QuizAttemptResultsDTO();
        mockResultsDTO.setAnswers(mockQuizAttempt.getAnswers());
        mockResultsDTO.setScore(mockQuizAttempt.getScore());
        mockResultsDTO.setAttemptTime(mockQuizAttempt.getAttemptTime());
        mockResultsDTO.setQuiz(new Quiz());
        when(quizAttemptService.getResults(courseId, studentId, quizId)).thenReturn(mockResultsDTO);

        // Mock JWT token extraction
        when(jwtService.extractUsername(anyString())).thenReturn(userId);

        // Perform the test
        mockMvc.perform(post("/quizzes/{courseId}/{quizId}/students/{studentId}", courseId, quizId, studentId)
                .header("Authorization", "Bearer validToken")
                .contentType("application/json")
                .content("{\"answers\":[\"answer\"]}"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                })  // Debugging response
                .andExpect(jsonPath("$.answers").value(mockQuizAttempt.getAnswers()))  // Corrected the assertion
                .andExpect(jsonPath("$.score").value(mockQuizAttempt.getScore())); // Optionally, add a check for score

        // Verify the submit and getResults methods were called
        verify(quizAttemptService, times(1)).submit(courseId, studentId, quizId, attemptDTO, userId);
        verify(quizAttemptService, times(1)).getResults(courseId, studentId, quizId);
    }

    @Test
    void testShowQuizResultForStudent() throws Exception {
        String courseId = "course123";
        String quizId = "quiz123";
        String studentId = "student123";

        // Prepare a mock QuizAttempt
        QuizAttempt mockQuizAttempt = new QuizAttempt();
        mockQuizAttempt.setCourseId(courseId);
        mockQuizAttempt.setStudentId(studentId);
        mockQuizAttempt.setQuiz(new Quiz());
        mockQuizAttempt.setAnswers(List.of("answer"));
        mockQuizAttempt.setScore(1);
        mockQuizAttempt.setAttemptTime(LocalDateTime.now());

        // Prepare a mock result DTO
        QuizAttemptResultsDTO mockResultsDTO = new QuizAttemptResultsDTO();
        mockResultsDTO.setAnswers(mockQuizAttempt.getAnswers());
        mockResultsDTO.setScore(mockQuizAttempt.getScore());
        mockResultsDTO.setAttemptTime(mockQuizAttempt.getAttemptTime());
        mockResultsDTO.setQuiz(new Quiz());

        // Mock the getResults method to return the mock results DTO
        when(quizAttemptService.getResults(courseId, studentId, quizId)).thenReturn(mockResultsDTO);

        // Mock JWT token extraction
        String userId = "student123";
        when(jwtService.extractUsername(anyString())).thenReturn(userId);

        // Perform the test
        mockMvc.perform(get("/quizzes/{courseId}/{quizId}/students/{studentId}/result", courseId, quizId, studentId)
                .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                })  // Debugging response
                .andExpect(jsonPath("$.answers").value(List.of("answer")))  // Expect answers to be returned
                .andExpect(jsonPath("$.score").value(1))  // Expect the score to be 1
                .andExpect(jsonPath("$.quiz").exists());  // Ensure quiz is present in the response

        // Verify that the getResults method was called
        verify(quizAttemptService, times(1)).getResults(courseId, studentId, quizId);
    }
}
