package com.example.btportal.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class LearnerQuizAttempt {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Quiz quiz;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double score;

    @Column(columnDefinition = "TEXT")
    private String feedback;
}
