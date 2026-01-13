package com.example.btportal.repository;

import com.example.btportal.model.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {

    // Find all submissions for a specific assignment
    List<AssignmentSubmission> findByAssignmentId(Long assignmentId);

    // Find all submissions from a specific learner
    List<AssignmentSubmission> findByLearnerId(Long learnerId);

    // Find a specific submission by a specific learner for a specific assignment
    Optional<AssignmentSubmission> findByAssignmentIdAndLearnerId(Long assignmentId, Long learnerId);
}