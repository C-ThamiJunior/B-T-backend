package com.example.btportal.controller;

import com.example.btportal.dto.QuizDTO;
import com.example.btportal.dto.QuestionDTO;
import com.example.btportal.mapper.QuizMapper;
import com.example.btportal.model.*;
import com.example.btportal.service.LessonService;
import com.example.btportal.service.ModuleService;
import com.example.btportal.service.QuizService;
import com.example.btportal.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final LessonService lessonService;
    private final ModuleService moduleService;
    private final QuestionService questionService;

    // ✅ Get all quizzes with Total Marks calculated
    @GetMapping
    public List<QuizDTO> getAllQuizzes() {
        return quizService.getAllQuizzes()
                .stream()
                .map(quiz -> {
                    QuizDTO dto = QuizMapper.toDTO(quiz);

                    // ✅ Calculate Total Marks
                    // Fetch questions for this quiz and sum their points
                    List<Question> questions = questionService.getQuestionsForQuiz(quiz.getId());
                    int total = questions.stream().mapToInt(Question::getPoints).sum();

                    dto.setTotalMarks(total); // Set the calculated total
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ... (Keep the rest of your existing methods like createQuiz, updateQuiz, deleteQuiz as they are)

    @PutMapping("/{id}")
    public ResponseEntity<QuizDTO> updateQuiz(@PathVariable Long id, @RequestBody QuizDTO dto) {
        Quiz updatedQuiz = quizService.updateQuiz(id, dto);
        return ResponseEntity.ok(QuizMapper.toDTO(updatedQuiz));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuiz(@PathVariable Long id) {
        return quizService.getQuiz(id)
                .map(QuizMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<QuizDTO> createQuiz(@RequestBody QuizDTO dto) {
        Lesson lesson;
        if (dto.getLessonId() != null) {
            lesson = lessonService.getLesson(dto.getLessonId())
                    .orElseThrow(() -> new RuntimeException("Lesson not found"));
        } else if (dto.getModuleId() != null) {
            com.example.btportal.model.Module module = moduleService.getModuleById(dto.getModuleId())
                    .orElseThrow(() -> new RuntimeException("Module not found"));

            lesson = new Lesson();
            lesson.setTitle(dto.getTitle());
            lesson.setDescription(dto.getDescription());
            lesson.setModule(module);
            lesson.setContentType(ContentType.QUIZ);
            lesson.setOrderIndex(0);

            lesson = lessonService.createLesson(lesson);
        } else {
            throw new RuntimeException("Quiz must be linked to a Lesson ID or Module ID");
        }

        Quiz quiz = QuizMapper.toEntity(dto, lesson);
        quiz.setLesson(lesson);
        Quiz savedQuiz = quizService.createQuiz(quiz);

        if (dto.getQuestions() != null && !dto.getQuestions().isEmpty()) {
            for (QuestionDTO qDto : dto.getQuestions()) {
                qDto.setQuizId(savedQuiz.getId());
                questionService.createQuestion(qDto);
            }
        }

        return ResponseEntity.ok(QuizMapper.toDTO(savedQuiz));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }
}