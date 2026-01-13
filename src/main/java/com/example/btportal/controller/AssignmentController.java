package com.example.btportal.controller;

import com.example.btportal.model.Assignment;
import com.example.btportal.model.AssignmentSubmission;
import com.example.btportal.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    // ✅ POST /api/assignments - This is what your frontend button will call
    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment) {
        // The 'assignment' body will come from the frontend form
        // It needs { title, description, courseId, moduleId, facilitatorId, dueDate, totalMarks }
        Assignment savedAssignment = assignmentService.createAssignment(assignment);
        return ResponseEntity
                .created(URI.create("/api/assignments/" + savedAssignment.getId()))
                .body(savedAssignment);
    }

    // ✅ GET /api/assignments - To get all assignments for the UI
    @GetMapping
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    // ✅ GET /api/assignments/{id} - To get a single one
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        return assignmentService.getAssignmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ GET /api/assignments/module/{moduleId} - Useful for your grading tab
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByModule(@PathVariable Long moduleId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByModuleId(moduleId));
    }

    // ✅ DELETE /api/assignments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        try {
            assignmentService.deleteAssignment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}