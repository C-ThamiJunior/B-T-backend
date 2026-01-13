package com.example.btportal.controller;

import com.example.btportal.model.AssignmentSubmission;
import com.example.btportal.service.AssignmentSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/submissions/assignment")
@RequiredArgsConstructor
public class AssignmentSubmissionController {

    private final AssignmentSubmissionService submissionService;

    // ✅ POST /api/submissions/assignment - Called by a LEARNER
    @PostMapping
    public ResponseEntity<AssignmentSubmission> submitAssignment(@RequestBody AssignmentSubmission submission) {
        // Body needs: { "assignmentId": 1, "learnerId": 1, "facilitatorId": 1, "submissionText": "..." }
        AssignmentSubmission savedSubmission = submissionService.submitAssignment(submission);
        return ResponseEntity.ok(savedSubmission);
    }

    // ✅ PUT /api/submissions/assignment/{id}/grade - Called by a FACILITATOR
    @PutMapping("/{id}/grade")
    public ResponseEntity<AssignmentSubmission> gradeAssignment(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {

        // Payload from frontend: { "score": 90, "feedback": "Good work!", "graderId": 1 }
        Integer score = (Integer) payload.get("score");
        String feedback = (String) payload.get("feedback");
        Long graderId = ((Number) payload.get("graderId")).longValue(); // Get as Number, then long

        if (score == null || graderId == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            AssignmentSubmission graded = submissionService.gradeSubmission(id, score, feedback, graderId);
            return ResponseEntity.ok(graded);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ GET /api/submissions/assignment/assignment/{id} - Called by FACILITATOR's Grading Tab
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<AssignmentSubmission>> getSubmissionsForAssignment(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsForAssignment(assignmentId));
    }

    // ✅ GET /api/submissions/assignment - To get ALL submissions
    @GetMapping
    public ResponseEntity<List<AssignmentSubmission>> getAllSubmissions() {
        return ResponseEntity.ok(submissionService.getAllSubmissions());
    }

    // ✅ GET /api/submissions/assignment/{id} - Get a single submission
    @GetMapping("/{id}")
    public ResponseEntity<AssignmentSubmission> getSubmissionById(@PathVariable Long id) {
        return submissionService.getSubmissionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}