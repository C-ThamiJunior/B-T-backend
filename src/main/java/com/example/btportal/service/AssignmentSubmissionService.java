package com.example.btportal.service;

import com.example.btportal.model.AssignmentSubmission;
import com.example.btportal.repository.AssignmentSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentSubmissionService {

    private final AssignmentSubmissionRepository submissionRepository;

    // Called by a LEARNER to submit their work
    public AssignmentSubmission submitAssignment(AssignmentSubmission submission) {
        // Check if a submission already exists for this user and assignment
        Optional<AssignmentSubmission> existing = submissionRepository
                .findByAssignmentIdAndLearnerId(submission.getAssignmentId(), submission.getLearnerId());

        if (existing.isPresent()) {
            // Update existing submission
            AssignmentSubmission subToUpdate = existing.get();
            subToUpdate.setSubmissionText(submission.getSubmissionText());
            subToUpdate.setSubmissionFileUrl(submission.getSubmissionFileUrl());
            subToUpdate.setSubmissionDate(LocalDateTime.now());
            // Clear old grade if resubmitting
            subToUpdate.setScore(null);
            subToUpdate.setFeedback(null);
            subToUpdate.setGraderId(null);
            subToUpdate.setGradedAt(null);
            return submissionRepository.save(subToUpdate);
        } else {
            // Create new submission
            submission.setSubmissionDate(LocalDateTime.now());
            return submissionRepository.save(submission);
        }
    }

    // Called by a FACILITATOR to grade the work
    public AssignmentSubmission gradeSubmission(Long submissionId, Integer score, String feedback, Long graderId) {
        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + submissionId));

        submission.setScore(score);
        submission.setFeedback(feedback);
        submission.setGraderId(graderId);
        submission.setGradedAt(LocalDateTime.now());

        return submissionRepository.save(submission);
    }

    // Get all submissions for one assignment (for the grading tab)
    public List<AssignmentSubmission> getSubmissionsForAssignment(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId);
    }

    public List<AssignmentSubmission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    // Get a specific submission
    public Optional<AssignmentSubmission> getSubmissionById(Long id) {
        return submissionRepository.findById(id);
    }
}