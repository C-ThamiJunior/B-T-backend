package com.example.btportal.service;

import com.example.btportal.model.Course;
import com.example.btportal.model.Module;
import com.example.btportal.repository.CourseRepository;
import com.example.btportal.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    // ✅ Create new module
    public Module createModule(Module module) {
        if (module.getCourse() != null && module.getCourse().getId() != null) {
            Course course = courseRepository.findById(module.getCourse().getId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            module.setCourse(course);
        }
        return moduleRepository.save(module);
    }

    // ✅ Update existing module
    public Module updateModule(Long id, Module updatedModule) {
        Module existing = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        existing.setTitle(updatedModule.getTitle());
        existing.setDescription(updatedModule.getDescription());
        existing.setOrderIndex(updatedModule.getOrderIndex());

        if (updatedModule.getCourse() != null && updatedModule.getCourse().getId() != null) {
            Course course = courseRepository.findById(updatedModule.getCourse().getId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            existing.setCourse(course);
        }

        return moduleRepository.save(existing);
    }

    // ✅ Get all modules
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    // ✅ Get module by ID
    public Optional<Module> getModuleById(Long id) {
        return moduleRepository.findById(id);
    }

    // ✅ Get all modules for a given course
    public List<Module> getModulesByCourse(Long courseId) {
        return moduleRepository.findByCourseId(courseId);
    }

    // ✅ Delete module
    public void deleteModule(Long id) {
        if (!moduleRepository.existsById(id)) {
            throw new RuntimeException("Module not found");
        }
        moduleRepository.deleteById(id);
    }
}
