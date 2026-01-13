package com.example.btportal.service;

import com.example.btportal.dto.QuestionDTO;
import com.example.btportal.model.AnswerOption;
import com.example.btportal.model.Question;
import com.example.btportal.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Question createQuestion(QuestionDTO dto) {
        Question question = new Question();

        // ✅ FIX 1: Use setText instead of setQuestionText
        question.setText(dto.getText());

        // ✅ FIX 2: Ensure we pass a String, not an Enum object
        // If dto.getQuestionType() is already a String, this works.
        // If it was an Enum, we would use dto.getQuestionType().name()
        question.setQuestionType(dto.getQuestionType());

        // ✅ FIX 3: Use setQuizId instead of setQuiz
        question.setQuizId(dto.getQuizId());

        question.setCorrectAnswer(dto.getCorrectAnswer());
        question.setPoints(dto.getPoints());

        // Handle Answer Options (Convert List<String> to List<AnswerOption>)
        if (dto.getOptions() != null) {
            List<AnswerOption> options = dto.getOptions().stream().map(optText -> {
                AnswerOption ao = new AnswerOption();
                ao.setOptionText(optText);
                return ao;
            }).collect(Collectors.toList());
            question.setOptions(options);
        }

        return questionRepository.save(question);
    }

    public List<Question> getQuestionsForQuiz(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }
}