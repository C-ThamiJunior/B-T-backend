package com.example.btportal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Lesson {
    @Id @GeneratedValue
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description; // ✅ Added

    private String url; // ✅ Added to store the link/file path

    private int orderIndex;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @ManyToOne
    private Module module;
}