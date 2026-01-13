package com.example.btportal.service;

import com.example.btportal.dto.QuestionDTO;
import com.example.btportal.dto.QuizDTO;
import com.example.btportal.model.Lesson;
import com.example.btportal.model.Question;
import com.example.btportal.model.Quiz;
import com.example.btportal.repository.QuestionRepository;
import com.example.btportal.repository.QuizRepository;
import com.example.btportal.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService; // Reuse existing service

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuiz(Long id) {
        return quizRepository.findById(id);
    }

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Transactional // Important for deleting old questions safely
    public Quiz updateQuiz(Long id, QuizDTO dto) {
        Quiz existing = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found"));

        // 1. Update Basic Info
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setTimed(dto.isTimed());
        existing.setTimeLimitInMinutes(dto.getTimeLimitInMinutes());

        // 2. Handle Questions (Simple Strategy: Replace All)
        if (dto.getQuestions() != null) {
            // Delete all existing questions for this quiz
            // Note: Ensure your QuestionRepository has a method to find/delete by Quiz,
            // or we iterate and delete.
            List<Question> oldQuestions = questionRepository.findByQuizId(id);
            questionRepository.deleteAll(oldQuestions);

            // Add new questions
            for (QuestionDTO qDto : dto.getQuestions()) {
                qDto.setQuizId(id); // Ensure they link to this quiz
                questionService.createQuestion(qDto);
            }
        }

        return quizRepository.save(existing);
    }

    public void deleteQuiz(Long id) {
        // Questions usually delete automatically if CascadeType.ALL is set on Entity,
        // otherwise delete manually:
        List<Question> questions = questionRepository.findByQuizId(id);
        questionRepository.deleteAll(questions);

        quizRepository.deleteById(id);
    }
}