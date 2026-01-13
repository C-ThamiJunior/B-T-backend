package com.example.btportal.dto;

import lombok.Data;
import java.util.Map;

@Data
public class QuizAttemptDTO {
    private Long quizId;
    private Long learnerId;
    // Map of <QuestionID, SubmittedAnswerText>
    private Map<Long, String> answers;
}