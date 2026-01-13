package com.example.btportal.repository;

import com.example.btportal.model.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional; // 👈 Import this
import java.util.List;   // 👈 And this (for the next step)

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {

    /**
     * Finds the correct answer for a given question.
     * This is the method you are missing.
     */
    Optional<AnswerOption> findByQuestionIdAndIsCorrectTrue(Long questionId);

    /**
     * Finds all answers for a given question.
     * (You will also need this for your QuestionService to work)
     */
    List<AnswerOption> findByQuestionId(Long questionId);

    /**
     * Deletes all answers associated with a specific question ID.
     * (You will need this for your QuestionService to delete a question)
     */
    void deleteByQuestionId(Long questionId);
}