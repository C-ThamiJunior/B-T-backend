package com.example.btportal.repository;

import com.example.btportal.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    // Find all attempts for a specific quiz (for the grading tab)
    List<QuizAttempt> findByQuizId(Long quizId);

    // Find all attempts by a specific learner
    List<QuizAttempt> findByLearnerId(Long learnerId);

    // Find a learner's specific attempt for a specific quiz
    Optional<QuizAttempt> findByQuizIdAndLearnerId(Long quizId, Long learnerId);
}