package com.example.btportal.controller;

import com.example.btportal.model.QuizAttempt;
import com.example.btportal.service.QuizAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.btportal.dto.QuizAttemptDTO; // Import the new DTO

@RestController
@RequestMapping("/api/attempts/quiz")
@RequiredArgsConstructor
public class QuizAttemptController {

    private final QuizAttemptService attemptService;

    // ✅ POST /api/attempts/quiz - Called by a LEARNER after completing a quiz
    @PostMapping
    public ResponseEntity<QuizAttempt> submitQuizAttempt(@RequestBody QuizAttempt attempt) {
        // Body needs: { "quizId": 1, "learnerId": 1, "score": 85, "totalMarks": 100 }
        QuizAttempt savedAttempt = attemptService.submitQuizAttempt(attempt);
        return ResponseEntity.ok(savedAttempt);
    }

    // ✅ GET /api/attempts/quiz/quiz/{quizId} - Called by FACILITATOR's Grading Tab
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuizAttempt>> getAttemptsForQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(attemptService.getAttemptsForQuiz(quizId));
    }

    // ✅ GET /api/attempts/quiz - To get ALL attempts
    @GetMapping
    public ResponseEntity<List<QuizAttempt>> getAllAttempts() {
        // You need to add 'findAll()' to your 'QuizAttemptService'
        // and 'QuizAttemptRepository'
        return ResponseEntity.ok(attemptService.getAllAttempts());
    }

    @PostMapping("/submit")
    public ResponseEntity<QuizAttempt> submitQuiz(@RequestBody QuizAttemptDTO submission) {
        QuizAttempt gradedAttempt = attemptService.submitAndGradeQuiz(submission);
        return ResponseEntity.ok(gradedAttempt);
    }

    // ✅ GET /api/attempts/quiz/{id} - Get a single attempt
    @GetMapping("/{id}")
    public ResponseEntity<QuizAttempt> getAttemptById(@PathVariable Long id) {
        return attemptService.getAttemptById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}