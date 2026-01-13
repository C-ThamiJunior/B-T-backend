package com.example.btportal.mapper;

import com.example.btportal.dto.LessonDTO;
import com.example.btportal.model.ContentType;
import com.example.btportal.model.Lesson;
import com.example.btportal.model.Module;

public class LessonMapper {

    public static LessonDTO toDTO(Lesson lesson) {
        if (lesson == null) return null;
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setDescription(lesson.getDescription()); // ✅ Map description
        dto.setUrl(lesson.getUrl());                 // ✅ Map URL
        dto.setContentType(lesson.getContentType() != null ? String.valueOf(lesson.getContentType()) : null);
        dto.setOrderIndex(lesson.getOrderIndex());
        dto.setModuleId(lesson.getModule() != null ? lesson.getModule().getId() : null);
        return dto;
    }

    public static Lesson toEntity(LessonDTO dto, Module module) {
        if (dto == null) return null;
        Lesson lesson = new Lesson();
        lesson.setId(dto.getId());
        lesson.setTitle(dto.getTitle());
        lesson.setDescription(dto.getDescription()); // ✅ Map description
        lesson.setUrl(dto.getUrl());                 // ✅ Map URL

        // Handle Enum safely
        if (dto.getContentType() != null) {
            lesson.setContentType(ContentType.valueOf(dto.getContentType()));
        }

        // Handle null orderIndex safely
        lesson.setOrderIndex(dto.getOrderIndex() != null ? dto.getOrderIndex() : 0);

        lesson.setModule(module);
        return lesson;
    }
}