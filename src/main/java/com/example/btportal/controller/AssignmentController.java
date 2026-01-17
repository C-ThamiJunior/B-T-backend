package com.example.btportal.controller;

import com.example.btportal.model.Assignment;
import com.example.btportal.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AssignmentController {

    private final AssignmentService assignmentService;

    // ✅ UPDATED POST: Handles Multipart Data (File + Text)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Assignment> createAssignment(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("courseId") Long courseId,
            @RequestParam("moduleId") Long moduleId,
            @RequestParam("facilitatorId") Long facilitatorId,
            @RequestParam("dueDate") String dueDate, // Received as String, parsed below
            @RequestParam("totalMarks") int totalMarks,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            Assignment assignment = new Assignment();
            assignment.setTitle(title);
            assignment.setDescription(description);
            assignment.setCourseId(courseId);
            assignment.setModuleId(moduleId);
            assignment.setFacilitatorId(facilitatorId);
            assignment.setTotalMarks(totalMarks);
            assignment.setDueDate(LocalDateTime.parse(dueDate)); // Parse ISO date string
            assignment.setActive(true);

            Assignment savedAssignment = assignmentService.createAssignment(assignment, file);

            return ResponseEntity
                    .created(URI.create("/api/assignments/" + savedAssignment.getId()))
                    .body(savedAssignment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        return assignmentService.getAssignmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByModule(@PathVariable Long moduleId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByModuleId(moduleId));
    }

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