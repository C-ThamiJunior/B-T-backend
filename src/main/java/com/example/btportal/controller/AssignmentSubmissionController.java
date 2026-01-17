package com.example.btportal.controller;

import com.example.btportal.model.Assignment;
import com.example.btportal.model.AssignmentSubmission;
import com.example.btportal.model.User;
import com.example.btportal.model.FileDocument;
import com.example.btportal.repository.AssignmentRepository;
import com.example.btportal.repository.AssignmentSubmissionRepository;
import com.example.btportal.repository.FileDocumentRepository;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitAssignment(
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("studentId") Long studentId,
            @RequestParam(value = "comments", required = false) String comments,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            // 1. Validate & Fetch Student Object
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            // 2. Validate & Fetch Assignment Object
            Assignment assignment = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));

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

            // 4. Create Submission Object
            AssignmentSubmission submission = new AssignmentSubmission();

            // ✅ FIX: Set the actual OBJECTS, not the IDs
            // The compilation error happened because we tried to set IDs directly.
            submission.setAssignment(assignment);
            submission.setStudent(student);

            // Note: facilitatorId seems to be a raw ID in your entity, so this is fine.
            submission.setFacilitatorId(assignment.getFacilitatorId());

            submission.setSubmissionText(comments);
            submission.setFileUrl(fileUrl);
            submission.setSubmissionDate(LocalDateTime.now());

            submissionRepository.save(submission);

            return ResponseEntity.ok(Map.of("message", "Submission successful"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AssignmentSubmission>> getAllSubmissions() {
        return ResponseEntity.ok(submissionRepository.findAll());
    }

    @PutMapping("/{id}/grade")
    public ResponseEntity<?> gradeSubmission(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        AssignmentSubmission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        Object gradeObj = body.get("grade");
        int grade = gradeObj instanceof Integer ? (Integer) gradeObj : Integer.parseInt(gradeObj.toString());

        submission.setGrade(grade);
        submission.setFeedback((String) body.get("feedback"));
        submission.setGradedAt(LocalDateTime.now());

        submissionRepository.save(submission);
        return ResponseEntity.ok(submission);
    }
}