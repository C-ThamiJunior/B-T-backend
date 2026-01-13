package com.example.btportal.service;

import com.example.btportal.model.AssignmentSubmission;
import com.example.btportal.repository.AssignmentSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentSubmissionService {

    @Autowired
    private AssignmentSubmissionRepository submissionRepository;

    public List<AssignmentSubmission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    // ✅ FIX: Updated to match new Entity structure
    public AssignmentSubmission saveSubmission(AssignmentSubmission submission) {
        // Check if a submission already exists for this Student + Assignment
        // We use .getAssignment().getId() and .getStudent().getId() now
        Optional<AssignmentSubmission> existing = submissionRepository.findByAssignmentIdAndStudentId(
                submission.getAssignment().getId(),
                submission.getStudent().getId()
        );

        if (existing.isPresent()) {
            AssignmentSubmission subToUpdate = existing.get();
            // ✅ FIX: Use getFileUrl() (New name)
            if (submission.getFileUrl() != null) {
                subToUpdate.setFileUrl(submission.getFileUrl());
            }
            // ✅ FIX: Use getSubmissionText()
            if (submission.getSubmissionText() != null) {
                subToUpdate.setSubmissionText(submission.getSubmissionText());
            }
            // Reset grade on re-submission if you want
            // subToUpdate.setGrade(null);

            return submissionRepository.save(subToUpdate);
        }

        return submissionRepository.save(submission);
    }

    public List<AssignmentSubmission> getSubmissionsByAssignment(Long assignmentId) {
        // Assuming you have this custom query or filter manually
        return submissionRepository.findAll().stream()
                .filter(s -> s.getAssignment().getId().equals(assignmentId))
                .toList();
    }

    public List<AssignmentSubmission> getSubmissionsByStudent(Long studentId) {
        return submissionRepository.findAll().stream()
                .filter(s -> s.getStudent().getId().equals(studentId))
                .toList();
    }

    // ✅ FIX: Method to handle grading
    @Transactional
    public AssignmentSubmission gradeSubmission(Long id, Integer grade, String feedback) {
        AssignmentSubmission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        // ✅ FIX: Use setGrade instead of setScore
        submission.setGrade(grade);
        submission.setFeedback(feedback);

        return submissionRepository.save(submission);
    }
}