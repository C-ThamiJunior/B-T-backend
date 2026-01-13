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

    @Column(nullable = false)
    private Long assignmentId; // The assignment this is for

    @Column(nullable = false)
    private Long learnerId; // The User ID of the student

    @Column(nullable = false)
    private Long facilitatorId; // The User ID of the facilitator (for easy lookup)

    private LocalDateTime submissionDate = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String submissionText; // For text-based submissions

    private String submissionFileUrl; // For uploaded files

    // --- Grading Fields ---
    private Integer score; // The grade given by the facilitator

    @Column(columnDefinition = "TEXT")
    private String feedback; // Facilitator's comments

    private Long graderId; // User ID of the facilitator who graded it

    private LocalDateTime gradedAt;

    // You can link these with @ManyToOne if you wish
    // @ManyToOne
    // @JoinColumn(name = "assignmentId", insertable = false, updatable = false)
    // private Assignment assignment;
    //
    // @ManyToOne
    // @JoinColumn(name = "learnerId", insertable = false, updatable = false)
    // private User learner;
}