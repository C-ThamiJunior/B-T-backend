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

//    public Course createCourse(Long instructorId, Course course) {
//        User instructor = userRepository.findById(instructorId)
//                .orElseThrow(() -> new RuntimeException("Instructor not found"));
//        course.setInstructor(instructor);
//        course.setCreatedAt(LocalDateTime.now());
//        return courseRepository.save(course);
//    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    public Optional<Course> getCourse(Long id) {
        return courseRepository.findById(id);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
