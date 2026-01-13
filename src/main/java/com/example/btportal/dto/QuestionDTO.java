package com.example.btportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    private String text;
    private String questionType; // e.g. MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER
    private List<String> options; // for multiple-choice questions
    private String correctAnswer;
    private int points;
    private Long quizId; // reference to parent Quiz
}

