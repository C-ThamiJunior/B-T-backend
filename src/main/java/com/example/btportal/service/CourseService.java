package com.example.btportal.service;

import com.example.btportal.model.Course;
import com.example.btportal.model.User;
import com.example.btportal.repository.CourseRepository;
import com.example.btportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    // ✅ Create a new course
    public Course createCourse(Long facilitatorId, Course course) {
        User facilitator = userRepository.findById(facilitatorId)
                .orElseThrow(() -> new RuntimeException("facilitator not found"));
        course.setFacilitator(facilitator);
        course.setCreateAt(LocalDateTime.now());
        return courseRepository.save(course);
    }

    // ✅ Get all courses (the missing one)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // ✅ Get courses by instructor
    public List<Course> getCoursesByFacilitator(Long facilitatorId) {
        return courseRepository.findByFacilitatorId(facilitatorId);
    }

    // ✅ Get a single course
    public Optional<Course> getCourse(Long id) {
        return courseRepository.findById(id);
    }

    // ✅ Delete a course
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
