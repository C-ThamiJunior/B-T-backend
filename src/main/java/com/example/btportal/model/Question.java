package com.example.btportal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue
    private Long id;

    private String questionText;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @ManyToOne
    private Quiz quiz;
}
