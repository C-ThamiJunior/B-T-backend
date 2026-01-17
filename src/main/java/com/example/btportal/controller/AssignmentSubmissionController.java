package com.example.btportal.controller;

import com.example.btportal.model.Assignment;
import com.example.btportal.model.AssignmentSubmission;
import com.example.btportal.model.User;
import com.example.btportal.model.FileDocument;
import com.example.btportal.repository.AssignmentRepository;
import com.example.btportal.repository.AssignmentSubmissionRepository;
import com.example.btportal.repository.FileDocumentRepository; // Needed for file saving
import com.example.btportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/submissions/assignment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AssignmentSubmissionController {

    private final AssignmentSubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final FileDocumentRepository fileDocumentRepository;

    // ✅ FIX: Match parameters exactly to StudentDashboard.tsx
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitAssignment(
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("studentId") Long studentId,
            @RequestParam(value = "comments", required = false) String comments,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            // 1. Validate User & Assignment
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Assignment assignment = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));

            // 2. Check if already submitted (Optional: prevent duplicates)
            // Optional<AssignmentSubmission> existing = submissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentId);
            // if (existing.isPresent()) return ResponseEntity.badRequest().body("Already submitted");

            // 3. Handle File Upload
            String fileUrl = null;
            if (file != null && !file.isEmpty()) {
                String uniqueName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                FileDocument fileDoc = new FileDocument();
                fileDoc.setFileName(uniqueName);
                fileDoc.setFileType(file.getContentType());
                fileDoc.setData(file.getBytes());
                fileDocumentRepository.save(fileDoc);

                fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/files/")
                        .path(uniqueName)
                        .toUriString();
            }

            // 4. Create Submission
            AssignmentSubmission submission = new AssignmentSubmission();
            submission.setAssignmentId(assignmentId);
            submission.setStudentId(studentId);
            submission.setSubmissionText(comments); // Map 'comments' to 'submissionText'
            submission.setFileUrl(fileUrl);
            submission.setSubmissionDate(LocalDateTime.now());

            // ✅ AUTO-FIX: Get facilitator ID from the Assignment itself
            submission.setFacilitatorId(assignment.getFacilitatorId());

            submissionRepository.save(submission);

            return ResponseEntity.ok(Map.of("message", "Submission successful"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // ... Keep your other GET/PUT methods (like gradeSubmission) below ...
    @GetMapping
    public ResponseEntity<List<AssignmentSubmission>> getAllSubmissions() {
        return ResponseEntity.ok(submissionRepository.findAll());
    }

    @PutMapping("/{id}/grade")
    public ResponseEntity<?> gradeSubmission(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        AssignmentSubmission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        submission.setGrade(Integer.parseInt(body.get("grade").toString()));
        submission.setFeedback((String) body.get("feedback"));
        submission.setGradedAt(LocalDateTime.now());

        submissionRepository.save(submission);
        return ResponseEntity.ok(submission);
    }
}   