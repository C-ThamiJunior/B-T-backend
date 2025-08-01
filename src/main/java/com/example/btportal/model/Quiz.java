package com.example.btportal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Quiz {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private boolean isTimed;
    private int timeLimitInMinutes;

    @OneToOne
    private Lesson lesson;
}
