package com.example.btportal.controller;

import com.example.btportal.dto.CourseDTO;
import com.example.btportal.mapper.CourseMapper;
import com.example.btportal.model.Course;
import com.example.btportal.model.User;
import com.example.btportal.service.CourseService;
import com.example.btportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    // ✅ Get all courses
    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses()
                .stream()
                .map(CourseMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Get course by ID
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable Long id) {
        return courseService.getCourse(id)
                .map(CourseMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO dto) {
        User Facilitator = userService.getUserById(dto.getFacilitatorId())
                .orElseThrow(() -> new RuntimeException("Facilitator not found"));

        Course course = CourseMapper.toEntity(dto, Facilitator);
        Course saved = courseService.createCourse(Facilitator.getId(), course);
        return ResponseEntity.ok(CourseMapper.toDTO(saved));
    }

    // ✅ Delete a course
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
