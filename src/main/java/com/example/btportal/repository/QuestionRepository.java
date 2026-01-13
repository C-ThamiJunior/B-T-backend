package com.example.btportal.repository;

import com.example.btportal.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    // ✅ This works because the field in Question.java is named 'quizId'
    List<Question> findByQuizId(Long quizId);
}