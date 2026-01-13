package com.example.btportal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    private User facilitator;

    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
    private boolean isPublished;
}
