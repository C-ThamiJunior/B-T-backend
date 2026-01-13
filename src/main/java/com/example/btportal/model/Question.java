package com.example.btportal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private String questionType; // e.g. MULTIPLE_CHOICE, TRUE_FALSE

    private String correctAnswer;
    private int points;

    // ✅ FIX: Add cascade = CascadeType.ALL, orphanRemoval = true
    // This ensures that when a Question is deleted, its AnswerOptions are also deleted.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "question_id") // Creates the foreign key in answer_option table
    private List<AnswerOption> options;

    private Long quizId;
}