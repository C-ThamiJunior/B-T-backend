package com.example.btportal.controller;

import com.example.btportal.model.Assignment;
import com.example.btportal.model.AssignmentSubmission;
import com.example.btportal.model.User;
import com.example.btportal.repository.AssignmentRepository;
import com.example.btportal.repository.UserRepository;
import com.example.btportal.service.AssignmentSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin(origins = "*") // Allow React Frontend to access
public class AssignmentSubmissionController {

    @Autowired
    private AssignmentSubmissionService submissionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    // ✅ POST: Submit an assignment
    // We receive IDs (studentId, assignmentId) and convert them to Objects
    @PostMapping("/assignment")
    public ResponseEntity<?> submitAssignment(@RequestBody Map<String, Object> payload) {
        try {
            Long studentId = Long.valueOf(payload.get("learnerId").toString());
            Long assignmentId = Long.valueOf(payload.get("assignmentId").toString());
            String fileUrl = (String) payload.get("fileUrl");
            String submissionText = (String) payload.get("submissionText");
            Long facilitatorId = Long.valueOf(payload.get("facilitatorId").toString());

            // 1. Fetch Entities
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            Assignment assignment = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));

            // 2. Create Submission Object
            AssignmentSubmission submission = new AssignmentSubmission();
            submission.setStudent(student);        // Set the User object
            submission.setAssignment(assignment);  // Set the Assignment object
            submission.setFacilitatorId(facilitatorId);
            submission.setFileUrl(fileUrl);
            submission.setSubmissionText(submissionText);
            submission.setSubmissionDate(LocalDateTime.now());

            // 3. Save
            AssignmentSubmission saved = submissionService.saveSubmission(submission);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error submitting assignment: " + e.getMessage());
        }
    }

    // ✅ GET: All submissions
    @GetMapping("/assignment")
    public List<AssignmentSubmission> getAllSubmissions() {
        return submissionService.getAllSubmissions();
    }

    // ✅ PUT: Grade a submission
    @PutMapping("/assignment/{id}/grade")
    public ResponseEntity<?> gradeSubmission(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            Integer grade = Integer.valueOf(payload.get("grade").toString());
            String feedback = (String) payload.get("feedback");

            AssignmentSubmission updated = submissionService.gradeSubmission(id, grade, feedback);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error grading submission: " + e.getMessage());
        }
    }
}