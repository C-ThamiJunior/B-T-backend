package com.example.btportal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Forum {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @ManyToOne
    private Course course;
}
