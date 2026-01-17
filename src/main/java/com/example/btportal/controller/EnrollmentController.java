package com.example.btportal.controller;

import com.example.btportal.model.Enrollment;
import com.example.btportal.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // ✅ Ensure Frontend can access this
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // POST /api/enrollments?userId=1&courseId=5
    @PostMapping
    public ResponseEntity<?> enroll(@RequestParam Long userId, @RequestParam Long courseId) {
        Enrollment enrollment = enrollmentService.enrollStudent(userId, courseId);
        return ResponseEntity.ok(Map.of("message", "Enrollment successful", "id", enrollment.getId()));
    }

    // GET /api/enrollments/user/1
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserEnrollments(@PathVariable Long userId) {
        return ResponseEntity.ok(enrollmentService.getStudentEnrollments(userId));
    }

    // ✅ ADD THIS ENDPOINT to fix the "Request method 'GET' is not supported" error
    @GetMapping
    public ResponseEntity<?> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }
}