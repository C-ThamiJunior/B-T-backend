package com.example.btportal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private Long moduleId;

    @Column(nullable = false)
    private Long facilitatorId;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime dueDate;

    private int totalMarks;

    @JsonProperty("isActive")
    private boolean isActive = true;

    // ✅ NEW: Field to store the file link
    private String fileUrl;
}