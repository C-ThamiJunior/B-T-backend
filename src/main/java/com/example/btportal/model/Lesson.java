package com.example.btportal.model;

import jakarta.persistence.*;

@Entity
public class Lesson {
    @Id @GeneratedValue
    private Long id;

    private String title;
    private int orderIndex;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @ManyToOne
    private Module module;
}
