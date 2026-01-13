package com.example.btportal.service;

import com.example.btportal.dto.QuizAttemptDTO;
import com.example.btportal.model.Question;
import com.example.btportal.model.QuizAttempt;
import com.example.btportal.repository.QuestionRepository;
import com.example.btportal.repository.QuizAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizAttemptService {

    private final QuizAttemptRepository attemptRepository;
    private final QuestionRepository questionRepository;

    // Called by a LEARNER when they finish a quiz
    public QuizAttempt submitQuizAttempt(QuizAttempt attempt) {
        Optional<QuizAttempt> existing = attemptRepository
                .findByQuizIdAndLearnerId(attempt.getQuizId(), attempt.getLearnerId());

        if (existing.isPresent()) {
            QuizAttempt attemptToUpdate = existing.get();
            attemptToUpdate.setScore(attempt.getScore());
            attemptToUpdate.setTotalMarks(attempt.getTotalMarks());
            attemptToUpdate.setAttemptDate(LocalDateTime.now());
            return attemptRepository.save(attemptToUpdate);
        } else {
            attempt.setAttemptDate(LocalDateTime.now());
            return attemptRepository.save(attempt);
        }
    }

    public List<QuizAttempt> getAttemptsForQuiz(Long quizId) {
        return attemptRepository.findByQuizId(quizId);
    }

    @Transactional
    public QuizAttempt submitAndGradeQuiz(QuizAttemptDTO submission) {
        // 1. Fetch all questions for this quiz
        List<Question> questions = questionRepository.findByQuizId(submission.getQuizId());

        int totalMarks = 0;
        int score = 0;

        for (Question question : questions) {
            // Add to total possible marks
            // Use 1 as default if points are 0 or null
            int qPoints = (question.getPoints() > 0) ? question.getPoints() : 1;
            totalMarks += qPoints;

            // Get the student's answer for this question ID
            // Note: submission.getAnswers() Key is Long (QuestionID), Value is String (Answer)
            String submittedAnswer = submission.getAnswers().get(question.getId());

            if (submittedAnswer == null) {
                continue; // Student skipped this question
            }

            // 2. Compare Strings (Case-insensitive mostly safer for typed answers)
            // We use the direct 'correctAnswer' field from the Question entity
            String correctAnswer = question.getCorrectAnswer();

            if (correctAnswer != null && correctAnswer.trim().equalsIgnoreCase(submittedAnswer.trim())) {
                score += qPoints;
            }
        }

        // 3. Save the attempt
        QuizAttempt newAttempt = new QuizAttempt();
        newAttempt.setQuizId(submission.getQuizId());
        newAttempt.setLearnerId(submission.getLearnerId());
        newAttempt.setScore(score);
        newAttempt.setTotalMarks(totalMarks);

        return submitQuizAttempt(newAttempt);
    }

    public List<QuizAttempt> getAllAttempts() {
        return attemptRepository.findAll();
    }

    public Optional<QuizAttempt> getAttemptById(Long id) {
        return attemptRepository.findById(id);
    }
}