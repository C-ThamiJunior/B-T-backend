package com.example.btportal.model;

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
    private String description; // From your frontend form

    @Column(nullable = false)
    private Long courseId; // To link to the Course

    @Column(nullable = false)
    private Long moduleId; // To link to the Module

    @Column(nullable = false)
    private Long facilitatorId; // The User ID of the creator

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime dueDate; // From your frontend form

    private int totalMarks; // From your frontend form

    private boolean isActive = true;

    // You can add relationships later, e.g.:
    // @ManyToOne
    // @JoinColumn(name = "courseId", insertable = false, updatable = false)
    // private Course course;
    //
    // @ManyToOne
    // @JoinColumn(name = "moduleId", insertable = false, updatable = false)
    // private Module module;
}