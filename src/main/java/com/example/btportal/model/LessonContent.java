package com.example.btportal.model;

import jakarta.persistence.*;

@Entity
public class LessonContent {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Lesson lesson;

    @Column(columnDefinition = "TEXT")
    private String contentText;

    private String videoUrl;
    private String downloadableResourceUrl;
    private boolean isDownloadable;
}
