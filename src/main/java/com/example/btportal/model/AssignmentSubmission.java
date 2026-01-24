package com.example.btportal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column(nullable = false)
    private Long facilitatorId;

    // ✅ FIX: Format date as String so Frontend can read it
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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

    // ✅ FIX: Expose flat assignmentId for the Frontend
    @JsonProperty("assignmentId")
    public Long getAssignmentId() {
        return assignment != null ? assignment.getId() : null;
    }

    // ✅ FIX: Expose flat studentId for the Frontend
    @JsonProperty("studentId")
    public Long getStudentId() {
        return student != null ? student.getId() : null;
    }
}