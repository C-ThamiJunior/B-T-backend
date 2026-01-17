package com.example.btportal.service;

import com.example.btportal.model.Course;
import com.example.btportal.model.Enrollment;
import com.example.btportal.model.User;
import com.example.btportal.repository.CourseRepository;
import com.example.btportal.repository.EnrollmentRepository;
import com.example.btportal.repository.UserRepository;
import com.example.btportal.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public Enrollment enrollStudent(Long userId, Long courseId) {
        if (enrollmentRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);

        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getStudentEnrollments(Long userId) {
        return enrollmentRepository.findByUserId(userId);
    }

    // ✅ ADD THIS METHOD
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
}