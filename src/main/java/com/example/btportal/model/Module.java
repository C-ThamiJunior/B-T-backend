package com.example.btportal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Module {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String description;
    private int orderIndex;

    @ManyToOne
    private Course course;
}

