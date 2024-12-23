package com.example.lms.course;

import com.example.lms.auth.JwtService;
import com.example.lms.lesson.Lesson;
import com.example.lms.lesson.LessonDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private CourseController courseController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    void testGetAvailableCourses() throws Exception {
        // Arrange
        String userId = "user123";
        when(jwtService.extractUsername(anyString())).thenReturn(userId);
        when(courseService.getAvailableCourses(userId)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/courses/available")
                .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        
        verify(courseService, times(1)).getAvailableCourses(userId);
    }

    @Test
    void testGetRelatedCourses() throws Exception {
        // Arrange
        String userId = "user123";
        when(jwtService.extractUsername(anyString())).thenReturn(userId);
        when(courseService.getRelatedCourses(userId)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/courses/related")
                .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        
        verify(courseService, times(1)).getRelatedCourses(userId);
    }

    @Test
    void testEnrollInCourse() throws Exception {
        // Arrange
        String userId = "user123";
        String courseId = "course123";
        doNothing().when(courseService).enrollInCourse(courseId, userId);
        when(jwtService.extractUsername(anyString())).thenReturn(userId);

        // Act & Assert
        mockMvc.perform(post("/courses/{courseId}/enroll", courseId)
                .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("Enrolled successfully"));
        
        verify(courseService, times(1)).enrollInCourse(courseId, userId);
    }

    @Test
    void testCreateCourse() throws Exception {
        // Arrange
        String userId = "user123";

        // Create CourseDto with the required fields
        CourseDto courseDto = new CourseDto();
        courseDto.setTitle("Course Title");
        courseDto.setDescription("Course Description");
        courseDto.setDuration(30);

        // Create Course object to be returned from the service
        Course course = new Course();
        course.setId("course123");
        course.setTitle("Course Title");
        course.setDescription("Course Description");
        course.setDuration(30);
        course.setInstructor(null);  // You can set a valid instructor if needed
        course.setStudents(Collections.emptyList());
        course.setLessons(Collections.emptyList());

        // Mocking the behavior of courseService to return the created Course
        when(courseService.createCourse(any(CourseDto.class), eq(userId))).thenReturn(course);

        // Mocking jwtService to return the userId
        when(jwtService.extractUsername(anyString())).thenReturn(userId);

        // Prepare the request body as JSON
        String jsonRequest = "{\"title\":\"Course Title\",\"description\":\"Course Description\",\"duration\":30}";

        // Act & Assert
        mockMvc.perform(post("/courses/create")
                .contentType("application/json")
                .header("Authorization", "Bearer validToken")
                .content(jsonRequest)) // Pass the correct JSON structure
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("course123"))
                .andExpect(jsonPath("$.title").value("Course Title"))
                .andExpect(jsonPath("$.description").value("Course Description"));

        // Verify the service was called with the correct parameters
        verify(courseService, times(1)).createCourse(any(CourseDto.class), eq(userId));
    }

    @Test
    void testGetEnrolledStudents() throws Exception {
        // Arrange
        String userId = "user123";
        String courseId = "course123";
        when(jwtService.extractUsername(anyString())).thenReturn(userId);
        when(courseService.getEnrolledStudents(courseId, userId)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/courses/{courseId}/students", courseId)
                .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        
        verify(courseService, times(1)).getEnrolledStudents(courseId, userId);
    }

    @Test
    void testAddLesson() throws Exception {
        // Arrange
        String userId = "user123";
        String courseId = "course123";

        // Create LessonDto with required fields
        LessonDto lessonDto = new LessonDto();
        lessonDto.setName("Lesson Title");
        lessonDto.setCourseId(courseId); // Set the courseId as it might be required in the DTO.

        // Mocking behavior of the courseService to handle addLesson
        doNothing().when(courseService).addLesson(eq(courseId), any(Lesson.class), eq(userId));

        // Mock the JWT service to return the userId
        when(jwtService.extractUsername(anyString())).thenReturn(userId);

        // Prepare the request body as JSON
        String jsonRequest = "{\"name\":\"Lesson Title\", \"courseId\":\"" + courseId + "\"}";

        // Act & Assert
        mockMvc.perform(post("/courses/{courseId}/add-lesson", courseId)
                .contentType("application/json")
                .header("Authorization", "Bearer validToken")
                .content(jsonRequest)) // Pass the correct JSON structure
                .andExpect(status().isOk())
                .andExpect(content().string("Lesson added successfully"));

        // Verify the service was called with the correct parameters
        verify(courseService, times(1)).addLesson(eq(courseId), any(Lesson.class), eq(userId));
    }

    @Test
    void testAddLesson_InvalidCourseId() throws Exception {
        // Arrange
        String userId = "user123";
        String invalidCourseId = "invalidCourseId";

        // Mocking behavior for invalid courseId
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"))
            .when(courseService).addLesson(eq(invalidCourseId), any(Lesson.class), eq(userId));

        // Mock the JWT service to return the userId
        when(jwtService.extractUsername(anyString())).thenReturn(userId);

        // Prepare the request body as JSON
        String jsonRequest = "{\"name\":\"Lesson Title\", \"courseId\":\"" + invalidCourseId + "\"}";

        // Act & Assert
        mockMvc.perform(post("/courses/{courseId}/add-lesson", invalidCourseId)
                .contentType("application/json")
                .header("Authorization", "Bearer validToken")
                .content(jsonRequest))
                .andExpect(status().isNotFound()) // Expect a 404 status
                .andExpect(content().string("Course not found")); // Expect the message "Course not found"

        // Verify the service was called
        verify(courseService, times(1)).addLesson(eq(invalidCourseId), any(Lesson.class), eq(userId));
    }
}
