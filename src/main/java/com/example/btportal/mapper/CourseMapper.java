package com.example.btportal.mapper;

import com.example.btportal.dto.CourseDTO;
import com.example.btportal.model.Course;
import com.example.btportal.model.User;

public class CourseMapper {

    public static CourseDTO toDTO(Course course) {
        if (course == null) return null;

        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setCreateAt(course.getCreateAt());

        if (course.getFacilitator() != null) {
            dto.setFacilitatorId(course.getFacilitator().getId());
            dto.setFacilitatorName(course.getFacilitator().getFullName());
        }

        return dto;
    }

    public static Course toEntity(CourseDTO dto, User facilitator) {
        if (dto == null) return null;

        Course course = new Course();
        course.setId(dto.getId());
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setCreateAt(dto.getCreateAt());
        course.setFacilitator(facilitator);

        return course;
    }
}
