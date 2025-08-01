package com.example.btportal.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Announcement {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @ManyToOne
    private Course course;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime createdAt;
}
