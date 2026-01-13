package com.example.btportal.controller;

import com.example.btportal.dto.QuestionDTO;
import com.example.btportal.model.Question;
import com.example.btportal.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    // Creates a new question and links it to a quiz
    @PostMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<Question> createQuestion(@PathVariable Long quizId, @RequestBody QuestionDTO dto) {
        dto.setQuizId(quizId); // Ensure DTO has the correct quizId from the path
        Question savedQuestion = questionService.createQuestion(dto);
        return ResponseEntity.ok(savedQuestion);
    }

    // Gets all questions for a specific quiz (for learners or facilitators)
    @GetMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<List<Question>> getQuestionsForQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(questionService.getQuestionsForQuiz(quizId));
    }

    // Deletes a question
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }
}