package com.example.btportal.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Enrollment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Course course;

    private LocalDateTime enrolledAt;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;
}
