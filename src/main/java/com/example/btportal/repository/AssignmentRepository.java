package com.example.btportal.repository;

import com.example.btportal.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    // Finds all assignments for a specific facilitator
    List<Assignment> findByFacilitatorId(Long facilitatorId);

    // Finds all assignments for a specific course
    List<Assignment> findByCourseId(Long courseId);

    // Finds all assignments for a specific module
    List<Assignment> findByModuleId(Long moduleId);
}