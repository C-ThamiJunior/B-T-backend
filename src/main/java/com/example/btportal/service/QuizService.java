package com.example.btportal.service;

import com.example.btportal.model.Question;
import com.example.btportal.model.Quiz;
import com.example.btportal.repository.QuestionRepository;
import com.example.btportal.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Question addQuestionToQuiz(Long quizId, Question question) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        question.setQuiz(quiz);
        return questionRepository.save(question);
    }

//    public List<Question> getQuestionsByQuiz(Long quizId) {
//        return questionRepository.findAll().stream()
//                .filter(q -> q.getQuiz().getId().equals(quizId))
//                .collect(Collectors.toList());
//    }
}
