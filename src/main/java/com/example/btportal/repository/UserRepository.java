package com.example.btportal.repository;

// User Repository
// src/main/java/com/lms/repository/UserRepository.java

import com.example.btportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for the User entity.
 * Provides standard CRUD (Create, Read, Update, Delete) operations
 * and allows for defining custom query methods based on method naming conventions.
 */
@Repository // Marks this interface as a Spring Data JPA repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their username.
     * Spring Data JPA automatically generates the query for this method.
     * @param email The username to search for.
     * @return An Optional containing the User if found, or empty if not.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the given username already exists.
     * @param ContactNumber The username to check.
     * @return true if a user with this username exists, false otherwise.
     */
    boolean existsByContactNumber(String contactNumber);

    /**
     * Checks if a user with the given email already exists.
     * @param email The email to check.
     * @return true if a user with this email exists, false otherwise.
     */
    boolean existsByEmail(String email);
}

