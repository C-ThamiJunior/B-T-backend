package com.example.btportal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Quiz {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    // ✅ ADDED DESCRIPTION FIELD
    @Column(length = 1000)
    private String description;

    private boolean isTimed;
    private int timeLimitInMinutes;

    @OneToOne
    private Lesson lesson;
}