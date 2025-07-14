package com.example.btportal.model;

// Role Enum
// src/main/java/com/lms/model/Role.java

/**
 * Defines the roles a user can have within the LMS.
 * This enum is used to control authorization and access levels.
 */
public enum Role {
    STUDENT,     // Can enroll in courses, view lessons, take quizzes.
    INSTRUCTOR,  // Can create and manage courses, lessons, and view student progress.
    ADMIN        // Has full administrative privileges, including user management.
}
