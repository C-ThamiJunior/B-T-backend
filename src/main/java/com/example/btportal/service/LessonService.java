package com.example.btportal.service;

import com.example.btportal.exception.ResourceNotFoundException;
import com.example.btportal.model.Lesson;
import com.example.btportal.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public Optional<Lesson> getLesson(Long id) {
        return lessonRepository.findById(id);
    }

    public Lesson createLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }


    public Lesson updateLesson(Long id, Lesson updatedLesson) {
        Lesson existing = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        existing.setTitle(updatedLesson.getTitle());
        existing.setDescription(updatedLesson.getDescription());
        existing.setUrl(updatedLesson.getUrl());
        existing.setContentType(updatedLesson.getContentType());
        existing.setOrderIndex(updatedLesson.getOrderIndex());

        // If module changed, update it too
        if (updatedLesson.getModule() != null) {
            existing.setModule(updatedLesson.getModule());
        }

        return lessonRepository.save(existing);
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }
}
