package com.example.btportal.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ForumPost {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Forum forum;

    @ManyToOne
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime postedAt;
}
