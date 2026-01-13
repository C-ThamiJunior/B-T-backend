package com.example.btportal.repository;

import com.example.btportal.model.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {

    // ✅ FIXED: Changed 'LearnerId' to 'StudentId'
    // This works because Spring knows 'student' has an 'id' field.
    Optional<AssignmentSubmission> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

    // If you have other methods using 'LearnerId', update them too:
    // List<AssignmentSubmission> findByLearnerId(Long learnerId);  <-- DELETE THIS
    List<AssignmentSubmission> findByStudentId(Long studentId); // <-- USE THIS
}