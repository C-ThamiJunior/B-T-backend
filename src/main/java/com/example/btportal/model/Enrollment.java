package com.example.btportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "enrollments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "course_id"})
})
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    // ✅ FIX: Stop recursion. Don't load User's other enrollments/courses
    @JsonIgnoreProperties({"enrollments", "submissions", "coursesCreated", "password"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    // ✅ FIX: Stop recursion. Don't load Course's enrollments/modules
    @JsonIgnoreProperties({"enrollments", "modules", "facilitator"})
    private Course course;

    private LocalDateTime enrollmentDate;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @PrePersist
    protected void onCreate() {
        enrollmentDate = LocalDateTime.now();
        if (status == null) status = EnrollmentStatus.ACTIVE;
    }
}