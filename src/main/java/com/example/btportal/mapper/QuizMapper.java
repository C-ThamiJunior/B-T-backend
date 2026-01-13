package com.example.btportal.mapper;

import com.example.btportal.dto.QuizDTO;
import com.example.btportal.model.Lesson;
import com.example.btportal.model.Quiz;

public class QuizMapper {

    public static QuizDTO toDTO(Quiz quiz) {
        if (quiz == null) return null;

        QuizDTO dto = new QuizDTO();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription()); // ✅ Map Description
        dto.setTimed(quiz.isTimed());
        dto.setTimeLimitInMinutes(quiz.getTimeLimitInMinutes());

        if (quiz.getLesson() != null) {
            dto.setLessonId(quiz.getLesson().getId());

            // ✅ Map Module and Course IDs via the Lesson
            if (quiz.getLesson().getModule() != null) {
                dto.setModuleId(quiz.getLesson().getModule().getId());

                if (quiz.getLesson().getModule().getCourse() != null) {
                    dto.setCourseId(quiz.getLesson().getModule().getCourse().getId());
                }
            }
        }

        return dto;
    }

    public static Quiz toEntity(QuizDTO dto, Lesson lesson) {
        if (dto == null) return null;

        Quiz quiz = new Quiz();
        quiz.setId(dto.getId());
        quiz.setTitle(dto.getTitle());
        quiz.setDescription(dto.getDescription()); // ✅ Map Description
        quiz.setTimed(dto.isTimed());
        quiz.setTimeLimitInMinutes(dto.getTimeLimitInMinutes());
        quiz.setLesson(lesson);

        return quiz;
    }
}