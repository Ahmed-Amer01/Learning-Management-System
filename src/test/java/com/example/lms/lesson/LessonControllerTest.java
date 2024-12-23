package com.example.lms.lesson;

import com.example.lms.auth.JwtService;
import com.example.lms.user.User;
import com.example.lms.common.enums.UserRole;
import com.example.lms.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LessonControllerTest {

    @Mock
    private LessonService lessonService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LessonController lessonController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController).build();
    }

    @Test
    void testCreateLesson() throws Exception {
        // Arrange
        String instructorId = "instructor123";
        LessonDto lessonDto = new LessonDto();
        lessonDto.setName("Lesson Title");
        lessonDto.setCourseId("course123");

        Lesson lesson = new Lesson();
        lesson.setId("lesson123");
        lesson.setName("Lesson Title");

        // Mocking the behavior for the JWT extraction and the user lookup
        when(jwtService.extractUsername(anyString())).thenReturn(instructorId);

        // Mocking the user repository to return a user with INSTRUCTOR role
        User mockUser = new User();
        mockUser.setId(instructorId);
        mockUser.setRole(UserRole.INSTRUCTOR);
        when(userRepository.findById(instructorId)).thenReturn(java.util.Optional.of(mockUser));

        // Mocking the lessonService to return the created lesson
        when(lessonService.createLesson(any(LessonDto.class), eq(instructorId))).thenReturn(lesson);

        // Prepare the request body as JSON
        String jsonRequest = "{\"name\":\"Lesson Title\", \"courseId\":\"course123\"}";

        // Act & Assert
        mockMvc.perform(post("/lessons/create")
                .contentType("application/json")
                .header("Authorization", "Bearer validToken")
                .content(jsonRequest))
                .andExpect(status().isCreated())  // Expect a 201 Created status
                .andExpect(jsonPath("$.id").value("lesson123"))
                .andExpect(jsonPath("$.name").value("Lesson Title"));

        // Verify the service was called with the correct parameters
        verify(lessonService, times(1)).createLesson(any(LessonDto.class), eq(instructorId));
    }


    @Test
    void testGenerateOtp() throws Exception {
        // Arrange
        String instructorId = "instructor123";
        String lessonId = "lesson123";

        // Mock behavior for JWT extraction and user lookup
        when(jwtService.extractUsername(anyString())).thenReturn(instructorId);

        // Mock the user repository to return a user with INSTRUCTOR role
        User mockUser = new User();
        mockUser.setId(instructorId);
        mockUser.setRole(UserRole.INSTRUCTOR);  // Set role to INSTRUCTOR
        when(userRepository.findById(instructorId)).thenReturn(java.util.Optional.of(mockUser));

        // Mock the generateOtp() to return a Lesson object
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setOtp("otp123");  // Set the OTP if it should be a part of the Lesson object

        // Using doReturn() to mock the behavior of the lessonService
        doReturn(lesson).when(lessonService).generateOtp(eq(lessonId), eq(instructorId));

        // Act & Assert
        mockMvc.perform(post("/lessons/{lessonId}/generate-otp", lessonId)
                .header("Authorization", "Bearer validToken")) // Ensure the Authorization header is passed
                .andExpect(status().isOk())  // Expect 200 OK
                .andExpect(jsonPath("$.otp").value("otp123"));  // Expect the otp field in the response JSON

        // Verify the service was called with the correct parameters
        verify(lessonService, times(1)).generateOtp(eq(lessonId), eq(instructorId));
    }

    @Test
    void testAttendLesson() throws Exception {
        // Arrange
        String studentId = "student123";
        String lessonId = "lesson123";

        // Mocking the behavior for the JWT extraction and user lookup
        when(jwtService.extractUsername(anyString())).thenReturn(studentId);

        // Mocking the user repository to return a user with STUDENT role
        User mockUser = new User();
        mockUser.setId(studentId);
        mockUser.setRole(UserRole.STUDENT);  // Ensure the role is set to STUDENT
        when(userRepository.findById(studentId)).thenReturn(java.util.Optional.of(mockUser));

        // Mocking the lessonService to handle the attendLesson logic
        doNothing().when(lessonService).attendLesson(eq(lessonId), eq("otp123"), eq(studentId));

        // Prepare the request body as JSON
        String jsonRequest = "{\"otp\":\"otp123\"}";

        // Act & Assert
        mockMvc.perform(post("/lessons/{lessonId}/attend", lessonId)
                .contentType("application/json")
                .header("Authorization", "Bearer validToken")
                .content(jsonRequest))
                .andExpect(status().isOk())  // Expect a 200 OK status
                .andExpect(content().string("Attendance marked successfully"));

        // Verify the service was called with the correct parameters
        verify(lessonService, times(1)).attendLesson(eq(lessonId), eq("otp123"), eq(studentId));
    }
}
