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

    // ✅ THIS IS THE FIX
    // The variable name MUST be "student" because User.java has mappedBy="student"
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // ✅ We also need the assignment relationship
    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column(nullable = false)
    private Long facilitatorId;

    private LocalDateTime submissionDate;

    @Column(columnDefinition = "TEXT")
    private String submissionText;

    private String fileUrl;

    // --- Grading Fields ---
    private Integer grade;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    private Long graderId;

    private LocalDateTime gradedAt;

    @PrePersist
    protected void onCreate() {
        this.submissionDate = LocalDateTime.now();
    }
}