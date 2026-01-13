package com.example.btportal.controller;

import com.example.btportal.repository.CourseRepository;
import com.example.btportal.repository.UserRepository;
// Import your Application/Posting repositories here
// import com.example.btportal.repository.PostApplicationRepository;
// import com.example.btportal.repository.GeneratePostApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    // Inject your AMS repositories
    // private final PostApplicationRepository applicationRepository;
    // private final GeneratePostApplicationRepository postingRepository;

    @GetMapping("/admin-dashboard")
    public ResponseEntity<Map<String, Long>> getAdminDashboardStats() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());
        stats.put("totalCourses", courseRepository.count());

        // Uncomment these once you inject the repos
        // stats.put("totalApplications", applicationRepository.count());
        // stats.put("totalPostings", postingRepository.count());

        return ResponseEntity.ok(stats);
    }
}