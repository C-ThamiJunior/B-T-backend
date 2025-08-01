package com.example.btportal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class AnswerOption {
    @Id
    @GeneratedValue
    private Long id;

    private String optionText;
    private boolean isCorrect;

    @ManyToOne
    private Question question;
}
