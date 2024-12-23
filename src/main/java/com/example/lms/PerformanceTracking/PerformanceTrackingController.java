package com.example.lms.PerformanceTracking;

import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class PerformanceTrackingController {

  @Autowired
  private PerformanceTrackingService performanceTrackingService;

  
  @GetMapping("/student/performance")
  @RolesAllowed({"INSTRUCTOR"})
  public ResponseEntity<?> getStudentPerformance(@RequestBody String studentId, HttpServletRequest request) {
    return performanceTrackingService.getStudentPerformance(studentId, request);
  }

  @GetMapping("/student/reports")
  @RolesAllowed({"ADMIN", "INSTRUCTOR"})
  public ResponseEntity<?> getStudentReport(@RequestBody String studentId, HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/vnd.ms-excel");

    String headerKey = "Content-Disposition";
    String headerValue = "attachment; filename=student_report.xlsx";

    response.setHeader(headerKey, headerValue);

    return performanceTrackingService.getStudentReport(studentId, request, response);
  }

}
