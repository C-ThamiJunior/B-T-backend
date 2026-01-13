package com.example.btportal.mapper;

import com.example.btportal.dto.ModuleDTO;
import com.example.btportal.model.Course;
import com.example.btportal.model.Module;

public class ModuleMapper {

    public static ModuleDTO toDTO(Module module) {
        if (module == null) {
            return null;
        }
        return new ModuleDTO(
                module.getId(),
                module.getTitle(),
                module.getDescription(),
                module.getOrderIndex(),
                module.getCourse() != null ? module.getCourse().getId() : null
        );
    }

    public static Module toEntity(ModuleDTO dto) {
        if (dto == null) {
            return null;
        }

        Module module = new Module();
        module.setId(dto.getId());
        module.setTitle(dto.getTitle());
        module.setDescription(dto.getDescription());

        // FIX: Check if orderIndex is null before assigning
        if (dto.getOrderIndex() != null) {
            module.setOrderIndex(dto.getOrderIndex());
        } else {
            module.setOrderIndex(0); // Default to 0 if missing
        }

        if (dto.getCourseId() != null) {
            Course course = new Course();
            course.setId(dto.getCourseId());
            module.setCourse(course);
        }

        return module;
    }
}