package com.example.btportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // ✅ CRITICAL IMPORT
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000) // Allow longer descriptions
    private String description;

    @ManyToOne
    @JoinColumn(name = "facilitator_id") // Good practice to name the column
    private User facilitator;

    // --- RELATIONSHIPS (These were missing or causing loops) ---

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonIgnore // ✅ STOPS CRASH: Course -> Module -> Course loop
    private List<Module> modules;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonIgnore // ✅ STOPS CRASH: Course -> Enrollment -> Course loop
    private List<Enrollment> enrollments;

    // --- TIMESTAMPS ---
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
    private boolean isPublished;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.isPublished = true; // Default to published or false depending on logic
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}