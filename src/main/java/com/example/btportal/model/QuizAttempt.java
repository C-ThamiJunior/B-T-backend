package com.example.btportal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long quizId; // The quiz that was taken

    @Column(nullable = false)
    private Long learnerId; // The User ID of the student

    @Column(nullable = false)
    private Integer score; // The score achieved (e.g., 80)

    private int totalMarks; // The total marks possible (e.g., 100)

    private LocalDateTime attemptDate = LocalDateTime.now();

    // You can add @ManyToOne relationships later if needed
    // @ManyToOne
    // @JoinColumn(name = "quizId", insertable = false, updatable = false)
    // private Quiz quiz;
    //
    // @ManyToOne
    // @JoinColumn(name = "learnerId", insertable = false, updatable = false)
    // private User learner;
}