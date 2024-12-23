package com.example.lms.PerformanceTracking;

import com.example.lms.PerformanceTracking.PerformanceTrackingController;
import com.example.lms.PerformanceTracking.PerformanceTrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Mockito;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PerformanceTrackingControllerTest {

    private MockMvc mockMvc;  // Do not use @Autowired

    @Mock
    private PerformanceTrackingService performanceTrackingService;

    @InjectMocks
    private PerformanceTrackingController performanceTrackingController;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the controller and mocks explicitly
        mockMvc = MockMvcBuilders.standaloneSetup(performanceTrackingController).build();
    }

    @Test
    void testGetStudentReport() throws Exception {
        String studentId = "student1";
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        // Mock the service response for getStudentReport
        when(performanceTrackingService.getStudentReport(anyString(), Mockito.any(), Mockito.any()))
                .thenReturn(ResponseEntity.ok("Report generated successfully"));

        // Simulate GET request for report generation
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/students/{studentId}/reports", studentId)
                        .param("studentId", studentId))  // Adding the request parameter
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())  // Expect HTTP 200 OK
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().string("Report generated successfully"));  // Expect the success message
    }

    @Test
    void testGetStudentPerformance() throws Exception {
        String studentId = "student1";
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        // Mock the service response for getStudentPerformance
        when(performanceTrackingService.getStudentPerformance(anyString(), Mockito.<HttpServletRequest>any()))
                .thenReturn(ResponseEntity.ok("Performance data"));

        // Simulate GET request for performance data
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/students/{studentId}/performance", studentId)
                        .param("studentId", studentId))  // Adding the request parameter
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())  // Expect HTTP 200 OK
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().string("Performance data"));  // Expect the performance data message
    }
}
