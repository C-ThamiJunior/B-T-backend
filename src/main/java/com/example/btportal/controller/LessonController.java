package com.example.btportal.controller;

import com.example.btportal.dto.LessonDTO;
import com.example.btportal.mapper.LessonMapper;
import com.example.btportal.model.Lesson;
import com.example.btportal.model.Module;
import com.example.btportal.service.LessonService;
import com.example.btportal.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final ModuleService moduleService;

    // ✅ Get all lessons
    @GetMapping
    public List<LessonDTO> getAllLessons() {
        return lessonService.getAllLessons()
                .stream()
                .map(LessonMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Get lesson by ID
    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getLesson(@PathVariable Long id) {
        return lessonService.getLesson(id)
                .map(LessonMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Create a new lesson
    @PostMapping
    public ResponseEntity<LessonDTO> createLesson(@RequestBody LessonDTO dto) {
        Module module = moduleService.getModuleById(dto.getModuleId())
                .orElseThrow(() -> new RuntimeException("Module not found"));

        Lesson lesson = LessonMapper.toEntity(dto, module);
        Lesson saved = lessonService.createLesson(lesson);
        return ResponseEntity.ok(LessonMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDTO> updateLesson(@PathVariable Long id, @RequestBody LessonDTO dto) {
        // We need the module to convert DTO to Entity correctly
        Module module = null;
        if (dto.getModuleId() != null) {
             module = moduleService.getModuleById(dto.getModuleId())
                    .orElseThrow(() -> new RuntimeException("Module not found"));
        }

        Lesson lessonUpdates = LessonMapper.toEntity(dto, module);
        Lesson saved = lessonService.updateLesson(id, lessonUpdates);
        return ResponseEntity.ok(LessonMapper.toDTO(saved));
    }


    // ✅ Delete a lesson
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}
