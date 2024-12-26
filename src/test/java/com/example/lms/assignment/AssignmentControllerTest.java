package com.example.lms.assignment;

import com.example.lms.assignment.Assignment;
import com.example.lms.assignment.AssignmentController;
import com.example.lms.assignment.AssignmentService;
import com.example.lms.assignment.AssignmentRepository;
import com.example.lms.course.Course;
import com.example.lms.course.CourseRepository;
import com.example.lms.course.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AssignmentControllerTest {

    @Mock
    private AssignmentService assignmentService;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private AssignmentController assignmentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(assignmentController).build();
    }

    @Test
    void testCreateAssignment() throws Exception {
        // Arrange
        String courseId = "course123";
        Assignment assignment = new Assignment();
        assignment.setTitle("Assignment Title");
        assignment.setDescription("Assignment Description");
        assignment.setDueDate(LocalDate.of(2024, 12, 31));
        assignment.setMaxGrade(100);

        Course course = new Course();
        course.setId(courseId);
        assignment.setCourse(course);

        // Mocking the service call
        when(assignmentService.createAssignment(eq(courseId), any(Assignment.class))).thenReturn(assignment);

        // Prepare the request body as JSON
        String jsonRequest = "{\"title\":\"Assignment Title\",\"description\":\"Assignment Description\",\"dueDate\":\"2024-12-31\",\"maxGrade\":100}";

        // Act & Assert
        mockMvc.perform(post("/courses/{courseId}/assignments", courseId)
                        .contentType("application/json")
                        .content(jsonRequest)) // Pass the correct JSON structure
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Assignment Title"))
                .andExpect(jsonPath("$.description").value("Assignment Description"))
                .andExpect(jsonPath("$.dueDate").value("2024-12-31"))
                .andExpect(jsonPath("$.maxGrade").value(100));

        verify(assignmentService, times(1)).createAssignment(eq(courseId), any(Assignment.class));
    }

    @Test
    void testGetAssignmentsByCourseId() throws Exception {
        // Arrange
        String courseId = "course123";
        Assignment assignment1 = new Assignment();
        assignment1.setTitle("Assignment 1");
        assignment1.setDescription("Description 1");
        assignment1.setDueDate(LocalDate.of(2024, 12, 25));
        assignment1.setMaxGrade(80);

        Assignment assignment2 = new Assignment();
        assignment2.setTitle("Assignment 2");
        assignment2.setDescription("Description 2");
        assignment2.setDueDate(LocalDate.of(2024, 12, 30));
        assignment2.setMaxGrade(90);

        List<Assignment> assignments = List.of(assignment1, assignment2);

        // Mocking the service call
        when(assignmentService.getAssignmentsByCourseId(courseId)).thenReturn(assignments);

        // Act & Assert
        mockMvc.perform(get("/courses/{courseId}/assignments", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Assignment 1"))
                .andExpect(jsonPath("$[1].title").value("Assignment 2"));

        verify(assignmentService, times(1)).getAssignmentsByCourseId(courseId);
    }
    @Test
    void testGetAssignmentsByCourseId_NoAssignments() throws Exception {
        // Arrange
        String courseId = "course123";
        when(assignmentService.getAssignmentsByCourseId(courseId)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/courses/{courseId}/assignments", courseId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(assignmentService, times(1)).getAssignmentsByCourseId(courseId);
    }
}
