package com.example.btportal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ FIX 1: Changed from 'Long learnerId' to 'User student'
    // This matches the mappedBy="student" in your User.java
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // ✅ FIX 2: Changed from 'Long assignmentId' to 'Assignment assignment'
    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column(nullable = false)
    private Long facilitatorId; // User ID of the facilitator (OK to keep as ID)

    private LocalDateTime submissionDate;

    @Column(columnDefinition = "TEXT")
    private String submissionText; // For text-based submissions

    private String fileUrl; // Renamed to match frontend logic usually (or keep submissionFileUrl)

    // --- Grading Fields ---
    private Integer grade; // Renamed from 'score' to match frontend usually

    @Column(columnDefinition = "TEXT")
    private String feedback; // Facilitator's comments

    private Long graderId;

    private LocalDateTime gradedAt;

    @PrePersist
    protected void onCreate() {
        this.submissionDate = LocalDateTime.now();
    }
}