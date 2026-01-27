package com.example.btportal.repository;

import com.example.btportal.model.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {

    // ✅ FIXED: Use explicit JPQL queries to avoid "assignmentId" property confusion

    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment.id = :assignmentId AND s.student.id = :studentId")
    Optional<AssignmentSubmission> findByAssignmentIdAndStudentId(@Param("assignmentId") Long assignmentId, @Param("studentId") Long studentId);

    @Query("SELECT s FROM AssignmentSubmission s WHERE s.student.id = :studentId")
    List<AssignmentSubmission> findByStudentId(@Param("studentId") Long studentId);
}