package com.example.btportal.service;

import com.example.btportal.model.Assignment;
import com.example.btportal.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    // Create a new assignment
    public Assignment createAssignment(Assignment assignment) {
        // You can add validation logic here, e.g., check if courseId exists
        return assignmentRepository.save(assignment);
    }

    // Get an assignment by its ID
    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    // Get all assignments
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    // Get all assignments for a specific module (for the grading tab)
    public List<Assignment> getAssignmentsByModuleId(Long moduleId) {
        return assignmentRepository.findByModuleId(moduleId);
    }

    // Delete an assignment
    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new RuntimeException("Assignment not found with id: " + id);
        }
        assignmentRepository.deleteById(id);
    }

    // You can add an updateAssignment method here later
}