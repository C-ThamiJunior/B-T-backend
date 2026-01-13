package com.example.btportal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn; // Good practice to be explicit
import com.fasterxml.jackson.annotation.JsonIgnore; // ✅ Import this
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AnswerOption {
    @Id
    @GeneratedValue
    private Long id;

    private String optionText;
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id") // Matches the @JoinColumn in Question.java
    @JsonIgnore // ✅ ADD THIS: Stops the infinite loop
    private Question question;
}